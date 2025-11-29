package com.example.biztrack.controller;

import com.example.biztrack.model.Client;
import com.example.biztrack.model.Income;
import com.example.biztrack.repository.ClientRepository;
import com.example.biztrack.repository.IncomeRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    private final IncomeRepository incomeRepository;
    private final ClientRepository clientRepository;

    public IncomeController(IncomeRepository incomeRepository,
                            ClientRepository clientRepository) {
        this.incomeRepository = incomeRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    // יצירת הכנסה שקשורה ללקוח (תשלום על פגישה/חבילה)
    @PostMapping("/client/{clientId}")
    public Income createClientIncome(@PathVariable Long clientId,
                                     @RequestParam BigDecimal amount,
                                     @RequestParam(required = false) String description) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        Income income = new Income();
        income.setClient(client);
        income.setAmount(amount);
        income.setDate(LocalDate.now());
        income.setSource("INTERNAL");
        income.setIncomeType("CLIENT_PAYMENT");
        income.setDescription(
                description != null && !description.isBlank()
                        ? description
                        : "Client payment from " + client.getName()
        );

        return incomeRepository.save(income);
    }
}
