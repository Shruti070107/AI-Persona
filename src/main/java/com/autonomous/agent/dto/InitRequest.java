package com.autonomous.agent.dto;

import com.autonomous.agent.model.Persona;

public class InitRequest {

    private Persona persona;

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
