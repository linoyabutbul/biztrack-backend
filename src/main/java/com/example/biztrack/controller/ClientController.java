package com.example.biztrack.controller;

import com.example.biztrack.model.Client;
import com.example.biztrack.model.ClientUpdateRequest;
import com.example.biztrack.repository.ClientRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // כל הלקוחות לטבלה בפרונט
    @GetMapping
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // לקוח אחד (אם תרצי להשתמש בזה בעתיד)
    @GetMapping("/{id}")
    public Client getClient(@PathVariable Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + id));
    }

    // יצירת לקוח חדש ידנית (לא חובה לפרונט כרגע, אבל טוב שיהיה)
    @PostMapping
    public Client createClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    // ✅ עדכון לקוח קיים – זה מה שהפרונט משתמש בו
    @PutMapping("/{id}")
    public Client updateClient(@PathVariable Long id,
                               @RequestBody ClientUpdateRequest request) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + id));

        if (request.getName() != null && !request.getName().isBlank()) {
            client.setName(request.getName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            client.setEmail(request.getEmail());
        }
        if (request.getCity() != null) {
            client.setCity(request.getCity());
        }
        if (request.getNotes() != null) {
            client.setNotes(request.getNotes());
        }

        return clientRepository.save(client);
    }

    // מחיקת לקוח (לעתיד, אם תרצי)
    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientRepository.deleteById(id);
    }
}
