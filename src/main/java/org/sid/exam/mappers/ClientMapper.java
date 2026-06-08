package org.sid.exam.mappers;

import org.sid.exam.dto.ClientDTO;
import org.sid.exam.entities.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientDTO toDTO(Client client) {
        if (client == null) {
            return null;
        }
        return new ClientDTO(client.getId(), client.getNom(), client.getEmail());
    }

    public Client toEntity(ClientDTO clientDTO) {
        if (clientDTO == null) {
            return null;
        }
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setNom(clientDTO.getNom());
        client.setEmail(clientDTO.getEmail());
        return client;
    }
}
