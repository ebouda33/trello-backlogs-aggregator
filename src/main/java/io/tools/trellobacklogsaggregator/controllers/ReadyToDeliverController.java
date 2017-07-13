package io.tools.trellobacklogsaggregator.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import io.tools.trellobacklogsaggregator.model.CardWithMembers;
import io.tools.trellobacklogsaggregator.repository.BacklogsRepository;

@Controller
public class ReadyToDeliverController extends AbstractController {

    @Autowired
    private BacklogsRepository backlogsRepository;

    @RequestMapping("/readytodeliver")
    public String index(Model model) {
        if (backlogsRepository.read() != null) {
            List<CardWithMembers> cardsReadyToDeliver = backlogsRepository.read().getCardsWithMembersReadyToDeliver();

            if (cardsReadyToDeliver != null && cardsReadyToDeliver.size() > 0) {
                model.addAttribute("cardsReadyToDeliver", cardsReadyToDeliver);
            }
            errorManagement(model);
        }
        return "readytodeliver";
    }
}
