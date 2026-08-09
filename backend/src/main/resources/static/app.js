(() => {
  const STORAGE_KEY = "signalDesk.agentId";

  const el = {
    bootScreen: document.getElementById("boot-screen"),
    desk: document.getElementById("desk"),
    initForm: document.getElementById("init-form"),
    initSubmit: document.getElementById("init-submit"),
    bootError: document.getElementById("boot-error"),
    nameInput: document.getElementById("persona-name"),
    domainSelect: document.getElementById("persona-domain-select"),
    customDomainField: document.getElementById("custom-domain-field"),
    customDomainInput: document.getElementById("persona-domain-custom"),

    statusDot: document.getElementById("status-dot"),
    nameDisplay: document.getElementById("persona-name-display"),
    domainDisplay: document.getElementById("persona-domain-display"),
    tagline: document.getElementById("persona-tagline"),
    statusValue: document.getElementById("status-value"),
    countdownValue: document.getElementById("countdown-value"),
    postCountValue: document.getElementById("post-count-value"),
    agentIdValue: document.getElementById("agent-id-value"),

    refreshBtn: document.getElementById("refresh-btn"),
    resetBtn: document.getElementById("reset-btn"),

    feed: document.getElementById("feed"),
    feedEmpty: document.getElementById("feed-empty"),
    feedError: document.getElementById("feed-error"),
    cardTemplate: document.getElementById("post-card-template"),
  };

  let agentId = localStorage.getItem(STORAGE_KEY);
  let nextPublishAtMs = null;
  let knownPostIds = new Set();
  let countdownTimer = null;
  let statusTimer = null;
  let feedTimer = null;

  init();

  function init() {
    el.domainSelect.addEventListener("change", onDomainChange);
    el.initForm.addEventListener("submit", onInitSubmit);
    el.refreshBtn.addEventListener("click", () => { fetchFeed(); fetchStatus(); });
    el.resetBtn.addEventListener("click", onReset);
    el.agentIdValue.addEventListener("click", copyAgentId);

    if (agentId) {
      showDesk();
      fetchStatus();
      fetchFeed();
      startPolling();
    } else {
      showBoot();
    }
  }

  // -----------------------------------------------------------------------
  // Boot / initialize
  // -----------------------------------------------------------------------

  function onDomainChange() {
    const isCustom = el.domainSelect.value === "__custom__";
    el.customDomainField.hidden = !isCustom;
    el.customDomainInput.required = isCustom;
  }

  async function onInitSubmit(e) {
    e.preventDefault();
    hideError(el.bootError);

    const name = el.nameInput.value.trim();
    const domain = el.domainSelect.value === "__custom__"
      ? el.customDomainInput.value.trim()
      : el.domainSelect.value;

    if (!name || !domain) {
      showError(el.bootError, "Both a name and a domain are required.");
      return;
    }

    setBusy(true);

    try {
      const res = await fetch("/api/agent/init", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ persona: { name, domain } }),
      });

      if (!res.ok) {
        const body = await safeJson(res);
        throw new Error(body?.error || `Init failed (${res.status})`);
      }

      const body = await res.json();
      agentId = body.agentId;
      localStorage.setItem(STORAGE_KEY, agentId);

      showDesk();
      fetchStatus();
      fetchFeed();
      startPolling();
    } catch (err) {
      showError(el.bootError, `Could not initialize: ${err.message}`);
    } finally {
      setBusy(false);
    }
  }

  function onReset() {
    localStorage.removeItem(STORAGE_KEY);
    agentId = null;
    knownPostIds = new Set();
    stopPolling();
    el.feed.innerHTML = "";
    showBoot();
  }

  // -----------------------------------------------------------------------
  // View switching
  // -----------------------------------------------------------------------

  function showBoot() {
    el.bootScreen.hidden = false;
    el.desk.hidden = true;
  }

  function showDesk() {
    el.bootScreen.hidden = true;
    el.desk.hidden = false;
  }

  // -----------------------------------------------------------------------
  // Polling
  // -----------------------------------------------------------------------

  function startPolling() {
    stopPolling();
    statusTimer = setInterval(fetchStatus, 8000);
    feedTimer = setInterval(fetchFeed, 20000);
    countdownTimer = setInterval(tickCountdown, 1000);
  }

  function stopPolling() {
    if (countdownTimer) clearInterval(countdownTimer);
    if (statusTimer) clearInterval(statusTimer);
    if (feedTimer) clearInterval(feedTimer);
  }

  // -----------------------------------------------------------------------
  // Status / countdown
  // -----------------------------------------------------------------------

  async function fetchStatus() {
    if (!agentId) return;
    try {
      const res = await fetch(`/api/agent/status?agentId=${encodeURIComponent(agentId)}`);
      if (res.status === 404) {
        onReset();
        return;
      }
      if (!res.ok) throw new Error(`status ${res.status}`);
      const s = await res.json();

      el.nameDisplay.textContent = s.name || "—";
      el.domainDisplay.textContent = s.domain || "—";
      el.tagline.textContent = s.tagline || "";
      el.postCountValue.textContent = String(s.postCount ?? 0);
      el.agentIdValue.textContent = shortId(s.agentId);
      el.agentIdValue.title = s.agentId;

      el.statusDot.classList.remove("is-error");
      el.statusValue.textContent = "online — operating autonomously";

      nextPublishAtMs = s.nextPublishAt ? Date.parse(s.nextPublishAt) : null;
      tickCountdown();
    } catch (err) {
      el.statusDot.classList.add("is-error");
      el.statusValue.textContent = "connection lost — retrying…";
    }
  }

  function tickCountdown() {
    if (!nextPublishAtMs) {
      el.countdownValue.textContent = "—";
      return;
    }
    const remainingMs = nextPublishAtMs - Date.now();
    if (remainingMs <= 0) {
      el.countdownValue.textContent = "due now…";
      return;
    }
    const totalSeconds = Math.floor(remainingMs / 1000);
    const h = Math.floor(totalSeconds / 3600);
    const m = Math.floor((totalSeconds % 3600) / 60);
    const s = totalSeconds % 60;
    el.countdownValue.textContent = h > 0
      ? `${h}h ${pad(m)}m`
      : `${pad(m)}m ${pad(s)}s`;
  }

  // -----------------------------------------------------------------------
  // Feed
  // -----------------------------------------------------------------------

  async function fetchFeed() {
    if (!agentId) return;
    try {
      const res = await fetch(`/api/agent/feed?agentId=${encodeURIComponent(agentId)}`);
      if (res.status === 404) {
        onReset();
        return;
      }
      if (!res.ok) throw new Error(`feed ${res.status}`);
      const body = await res.json();
      hideError(el.feedError);
      renderFeed(body.posts || []);
    } catch (err) {
      showError(el.feedError, "Signal lost — could not reach the desk. Retrying automatically.");
    }
  }

  function renderFeed(posts) {
    el.feedEmpty.hidden = posts.length > 0;
    if (posts.length === 0) {
      el.feed.innerHTML = "";
      return;
    }

    const total = posts.length;
    const frag = document.createDocumentFragment();

    posts.forEach((post, i) => {
      const isNew = knownPostIds.size > 0 && !knownPostIds.has(post.id);
      frag.appendChild(buildCard(post, total - i, isNew));
    });

    el.feed.innerHTML = "";
    el.feed.appendChild(frag);
    knownPostIds = new Set(posts.map((p) => p.id));
  }

  function buildCard(post, transmissionNumber, isNew) {
    const node = el.cardTemplate.content.cloneNode(true);
    const card = node.querySelector(".card");
    if (isNew) card.classList.add("is-new");

    node.querySelector(".card-index").textContent = `#${String(transmissionNumber).padStart(3, "0")}`;

    const time = node.querySelector(".card-time");
    const date = new Date(post.createdAt);
    time.dateTime = post.createdAt;
    time.textContent = isFinite(date) ? formatTimestamp(date) : post.createdAt;

    node.querySelector(".card-text").textContent = post.text;
    node.querySelector(".card-rationale-text").textContent = post.rationale;

    const sourcesEl = node.querySelector(".card-sources");
    (post.sources || []).forEach((url) => {
      const a = document.createElement("a");
      a.href = url;
      a.target = "_blank";
      a.rel = "noopener noreferrer";
      a.className = "source-chip";
      a.textContent = hostnameOf(url);
      sourcesEl.appendChild(a);
    });

    return node;
  }

  // -----------------------------------------------------------------------
  // Helpers
  // -----------------------------------------------------------------------

  function shortId(id) {
    if (!id) return "—";
    return id.length > 13 ? `${id.slice(0, 8)}…${id.slice(-4)}` : id;
  }

  function hostnameOf(url) {
    try {
      return new URL(url).hostname.replace(/^www\./, "");
    } catch {
      return url;
    }
  }

  function formatTimestamp(date) {
    return date.toISOString().replace("T", " ").slice(0, 16) + " UTC";
  }

  function pad(n) {
    return String(n).padStart(2, "0");
  }

  const initSubmitDefaultHtml = el.initSubmit.innerHTML;

  function setBusy(busy) {
    el.initSubmit.disabled = busy;
    el.initSubmit.innerHTML = busy ? "Initializing…" : initSubmitDefaultHtml;
  }

  function showError(node, message) {
    node.textContent = message;
    node.hidden = false;
  }

  function hideError(node) {
    node.hidden = true;
  }

  async function copyAgentId() {
    if (!agentId) return;
    try {
      await navigator.clipboard.writeText(agentId);
      el.agentIdValue.classList.add("copied");
      const prev = el.agentIdValue.textContent;
      el.agentIdValue.textContent = "copied";
      setTimeout(() => {
        el.agentIdValue.textContent = prev;
        el.agentIdValue.classList.remove("copied");
      }, 1200);
    } catch {
      /* clipboard unavailable — no-op */
    }
  }

  async function safeJson(res) {
    try {
      return await res.json();
    } catch {
      return null;
    }
  }
})();
