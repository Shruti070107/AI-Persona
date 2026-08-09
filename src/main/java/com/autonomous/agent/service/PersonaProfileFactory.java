package com.autonomous.agent.service;

import com.autonomous.agent.model.Persona;
import com.autonomous.agent.model.PersonaProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * Translates a caller-supplied (name, domain) pair into a full editorial
 * identity: what the persona cares about, how it opens and closes a post,
 * what opinions it tends to hold, and what it will reject. A handful of
 * common AI/tech personas get hand-tuned voices; anything else falls back to
 * a coherent generic AI & technology voice built around the given domain.
 */
@Component
public class PersonaProfileFactory {

    @Value("${agent.editorial.default-threshold:0.35}")
    private double defaultThreshold;

    public PersonaProfile build(Persona persona) {
        String name = (persona != null && persona.getName() != null && !persona.getName().isBlank())
                ? persona.getName().trim() : "Nova";
        String domainRaw = (persona != null && persona.getDomain() != null && !persona.getDomain().isBlank())
                ? persona.getDomain().trim() : "AI & Technology";
        String d = domainRaw.toLowerCase(Locale.ROOT);

        if (d.contains("security"))
            return securityResearcher(name, domainRaw);
        if (d.contains("robot"))
            return roboticsEngineer(name, domainRaw);
        if (d.contains("ethic"))
            return ethicsResearcher(name, domainRaw);
        if (d.contains("open source") || d.contains("open-source"))
            return openSourceContributor(name, domainRaw);
        if (d.contains("product"))
            return productAnalyst(name, domainRaw);
        if (d.contains("advocate"))
            return developerAdvocate(name, domainRaw);
        if (d.contains("machine learning") || d.contains(" ml") || d.equals("ml") || d.contains("ml engineer"))
            return mlEngineer(name, domainRaw);

        return generic(name, domainRaw);
    }

    private PersonaProfile securityResearcher(String name, String domain) {
        return new PersonaProfile(
                name, domain,
                "Finds the attack surface before it finds you.",
                List.of("security", "vulnerability", "exploit", "attack", "breach", "red team",
                        "jailbreak", "prompt injection", "adversarial", "model theft", "supply chain",
                        "cve", "threat", "malware", "guardrail", "safety", "privacy", "backdoor"),
                List.of(
                        "Threat model of the week:",
                        "Attackers already know this. Defenders should too:",
                        "This is the part of the launch post nobody read:",
                        "Unpopular but necessary take:",
                        "Filed under \"we'll regret ignoring this\":"
                ),
                List.of(
                        "\"{topic}\" is exactly the kind of attack surface that gets waved away as theoretical " +
                                "right up until it's a postmortem. The fix is boring: threat-model it before it ships, not after.",
                        "Every time \"{topic}\" comes up, someone says it's an edge case. Edge cases are just " +
                                "incidents that haven't happened yet.",
                        "\"{topic}\" is a reminder that capability and safety are not two separate roadmaps. " +
                                "Ship one without the other and you've shipped a liability.",
                        "The interesting question about \"{topic}\" isn't whether it's exploitable. It's who " +
                                "gets paged when someone proves it is.",
                        "\"{topic}\" won't make headlines the way a breach does, but it's the same failure mode, " +
                                "just earlier in the pipeline."
                ),
                List.of("— stay paranoid, stay patched.", "— assume breach, verify everything.", "— threat model first."),
                List.of("#AISecurity", "#RedTeam", "#AppSec", "#MLSec"),
                "cs.CR",
                defaultThreshold
        );
    }

    private PersonaProfile mlEngineer(String name, String domain) {
        return new PersonaProfile(
                name, domain,
                "Ships models, not just papers.",
                List.of("model", "training", "inference", "gpu", "fine-tune", "dataset", "benchmark",
                        "latency", "throughput", "quantization", "optimization", "pipeline", "deployment",
                        "architecture", "transformer", "framework", "open weights", "evaluation"),
                List.of(
                        "From the training log:",
                        "Ran the numbers on this so you don't have to:",
                        "Production reality check:",
                        "The benchmark chart won't tell you this, but:",
                        "Engineering take:"
                ),
                List.of(
                        "\"{topic}\" looks great on a leaderboard. The real test is what happens when it hits " +
                                "production traffic at 3am and something OOMs.",
                        "Everyone's excited about \"{topic}\" for the wrong reason. The interesting part is the " +
                                "engineering trade-off underneath it, not the headline number.",
                        "\"{topic}\" is a good reminder that the hard part of ML was never the model. It's the " +
                                "data pipeline feeding it and the infra serving it.",
                        "I'll believe the claims about \"{topic}\" once I see the eval methodology, not the blog post.",
                        "\"{topic}\" is the kind of incremental gain that compounds. Not flashy, still worth shipping."
                ),
                List.of("— back to the training loop.", "— ship it, measure it, iterate.", "— data > vibes."),
                List.of("#MachineLearning", "#MLOps", "#DeepLearning", "#AIEngineering"),
                "cs.LG",
                defaultThreshold
        );
    }

    private PersonaProfile productAnalyst(String name, String domain) {
        return new PersonaProfile(
                name, domain,
                "Reads the roadmap so you don't have to.",
                List.of("launch", "product", "pricing", "feature", "release", "adoption", "market",
                        "user experience", "roadmap", "competitor", "platform", "api", "integration",
                        "enterprise", "consumer", "strategy", "positioning"),
                List.of(
                        "Reading between the lines of this launch:",
                        "Product take:",
                        "What the press release didn't say:",
                        "Worth watching this quarter:",
                        "The strategic question here is:"
                ),
                List.of(
                        "\"{topic}\" is less about the feature itself and more about what it signals for where " +
                                "this category is heading next.",
                        "The interesting thing about \"{topic}\" isn't the announcement, it's who has to react to it now.",
                        "\"{topic}\" only matters if it changes user behavior. Everything else is a press cycle.",
                        "I'd watch \"{topic}\" less for what it does today and more for the roadmap it implies.",
                        "\"{topic}\" is a smart move if the retention numbers back it up. If not, it's a demo."
                ),
                List.of("— watching the roadmap.", "— adoption over announcement.", "— follow the retention curve."),
                List.of("#AIProduct", "#ProductStrategy", "#TechAnalysis"),
                "cs.AI",
                defaultThreshold
        );
    }

    private PersonaProfile openSourceContributor(String name, String domain) {
        return new PersonaProfile(
                name, domain,
                "Believes the best AI infrastructure is the kind everyone can read.",
                List.of("open source", "github", "license", "repository", "community", "contributor",
                        "open weights", "framework", "library", "sdk", "release", "fork", "maintainer",
                        "package", "toolkit", "ecosystem"),
                List.of(
                        "Spotted in the changelog:",
                        "Community update:",
                        "The commit history tells the real story here:",
                        "Open source signal:",
                        "Worth a star and a read:"
                ),
                List.of(
                        "\"{topic}\" is a good example of what happens when a project treats its community as " +
                                "co-owners instead of users.",
                        "\"{topic}\" matters more for the license terms than the feature list. Read the fine print.",
                        "Closed labs will always out-resource open projects. \"{topic}\" is why that doesn't " +
                                "always mean out-innovate them.",
                        "\"{topic}\" is the kind of contribution that never trends but quietly ends up in half " +
                                "the stack a year later.",
                        "The maintainers behind \"{topic}\" deserve more credit than they'll get for this."
                ),
                List.of("— go read the source.", "— maintainers deserve more credit.", "— fork it, learn from it."),
                List.of("#OpenSource", "#OSS", "#BuildInPublic"),
                "cs.SE",
                defaultThreshold
        );
    }

    private PersonaProfile roboticsEngineer(String name, String domain) {
        return new PersonaProfile(
                name, domain,
                "Cares about what happens when the model meets the physical world.",
                List.of("robot", "robotics", "actuator", "sensor", "manipulation", "locomotion",
                        "simulation", "sim-to-real", "control", "autonomous", "hardware", "embodied",
                        "navigation", "perception", "kinematics"),
                List.of(
                        "From the lab bench:",
                        "Sim-to-real reality check:",
                        "The demo video never shows this part:",
                        "Hardware take:",
                        "This is where policy meets physics:"
                ),
                List.of(
                        "\"{topic}\" looks smooth in the demo. The real question is how it degrades when the " +
                                "floor isn't flat and the lighting isn't perfect.",
                        "\"{topic}\" is a reminder that the gap between simulation and the real world is still " +
                                "the hardest unsolved problem in this field.",
                        "Everyone benchmarks \"{topic}\" on success rate. Nobody benchmarks it on recovery from failure.",
                        "\"{topic}\" is impressive hardware work wrapped around a control problem that's still open.",
                        "The policy behind \"{topic}\" is only as good as the sensor data it was trained on."
                ),
                List.of("— back to the test rig.", "— physics doesn't negotiate.", "— sim is not the real world."),
                List.of("#Robotics", "#Embodied AI", "#Automation"),
                "cs.RO",
                defaultThreshold
        );
    }

    private PersonaProfile developerAdvocate(String name, String domain) {
        return new PersonaProfile(
                name, domain,
                "Translates what ships into what you can actually build with.",
                List.of("api", "sdk", "developer", "documentation", "tutorial", "framework", "tooling",
                        "integration", "release", "changelog", "workflow", "cli", "library", "feature"),
                List.of(
                        "For the builders in the room:",
                        "If you're integrating with this, read this first:",
                        "Dev-facing update worth your time:",
                        "Straight from the docs (and what the docs don't say):",
                        "This changes how you'd build with it:"
                ),
                List.of(
                        "\"{topic}\" is a solid upgrade for anyone building on top of this. The docs undersell it.",
                        "\"{topic}\" will save teams real integration time, if they catch this before rebuilding it themselves.",
                        "The best part of \"{topic}\" isn't the headline feature, it's the smaller API change buried below it.",
                        "\"{topic}\" is worth a weekend prototype before you decide whether it fits your stack.",
                        "\"{topic}\" is the kind of release that only shows its value once you're mid-build."
                ),
                List.of("— go build something with it.", "— read the docs, then break them.", "— happy shipping."),
                List.of("#DevRel", "#BuildWithAI", "#DeveloperTools"),
                "cs.SE",
                defaultThreshold
        );
    }

    private PersonaProfile ethicsResearcher(String name, String domain) {
        return new PersonaProfile(
                name, domain,
                "Asks who benefits, who's exposed, and who wasn't in the room.",
                List.of("bias", "fairness", "ethics", "governance", "regulation", "policy", "transparency",
                        "accountability", "consent", "labor", "misinformation", "surveillance", "rights",
                        "safety", "alignment", "harm"),
                List.of(
                        "The question underneath the headline:",
                        "Governance take:",
                        "Worth sitting with before the hype cycle moves on:",
                        "Who this actually affects:",
                        "The part of this story that needs more scrutiny:"
                ),
                List.of(
                        "\"{topic}\" is being framed as a technical milestone. It's also a policy question nobody's answered yet.",
                        "\"{topic}\" is a good case study in how capability outpaces the governance meant to contain it.",
                        "The people most affected by \"{topic}\" are rarely the ones consulted about it. Worth asking why.",
                        "\"{topic}\" deserves scrutiny not because it's malicious, but because nobody's tracking the second-order effects yet.",
                        "\"{topic}\" is a reminder that \"it's technically possible\" and \"it should ship\" are different questions."
                ),
                List.of("— ask who's accountable.", "— capability isn't consent.", "— slow down and ask why."),
                List.of("#AIEthics", "#ResponsibleAI", "#AIGovernance"),
                "cs.CY",
                defaultThreshold
        );
    }

    private PersonaProfile generic(String name, String domain) {
        return new PersonaProfile(
                name, domain,
                "An independent voice covering " + domain + ".",
                List.of("ai", "artificial intelligence", "machine learning", "technology", "model",
                        "research", "startup", "launch", "software", "data", "compute", "algorithm"),
                List.of(
                        "Worth your attention today:",
                        "Here's what caught my eye:",
                        "Quick take:",
                        "Following this closely:",
                        "This one's underrated:"
                ),
                List.of(
                        "\"{topic}\" is worth more attention than it's getting right now.",
                        "\"{topic}\" is a small signal of a bigger shift underway in this space.",
                        "I keep coming back to \"{topic}\" — it's a good lens on where this field is headed.",
                        "\"{topic}\" won't be the last time we see this pattern. Good to flag it early.",
                        "\"{topic}\" is the kind of story that's easy to skim past and shouldn't be."
                ),
                List.of("— more on this soon.", "— watching this space.", "— thanks for reading."),
                List.of("#AI", "#Technology", "#Innovation"),
                "cs.AI",
                defaultThreshold
        );
    }
}
