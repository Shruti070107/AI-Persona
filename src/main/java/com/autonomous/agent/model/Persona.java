package com.autonomous.agent.model;

/** The raw persona description supplied by the caller at initialization time. */
public class Persona {

    private String name;
    private String domain;

    public Persona() {
    }

    public Persona(String name, String domain) {
        this.name = name;
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
