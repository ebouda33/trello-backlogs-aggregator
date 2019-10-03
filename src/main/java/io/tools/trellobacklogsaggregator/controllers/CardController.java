package io.tools.trellobacklogsaggregator.controllers;

import com.julienvey.trello.domain.Card;
import io.tools.trellobacklogsaggregator.execptions.ListException;
import io.tools.trellobacklogsaggregator.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @PutMapping("")
    public Card putcard(@RequestBody String data){


        return null;
    }

    @GetMapping("/{boardId}/{label}")
    public List<Card> getIndex(@PathVariable String boardId, @PathVariable String label) {
        return getCardList(boardId, label);

    }

    private List<Card> getCardList(String boardId, String label) {
        return getCardList(boardId, label, Optional.empty());
    }

    private List<Card> getCardList(String boardId, String label, Optional<String> idList) {
        try {
            final List<Card> cardsFromListsByLabel = this.cardService.getCardsFromListsByLabel(boardId, label);
            if (idList.isPresent()) {
                return cardsFromListsByLabel.stream().filter(card -> card.getIdList().equalsIgnoreCase(idList.get())).collect(Collectors.toList());
            }
            return this.cardService.getCardsFromListsByLabel(boardId, label);
        } catch (ListException exc) {
            final Card card = new Card();
            card.setName(exc.getMessage());
            return new ArrayList<Card>() {{
                add(card);
            }};
        } catch (RuntimeException exc) {
            final Card card = new Card();
            card.setName(exc.getMessage());
            return new ArrayList<Card>() {{
                add(card);
            }};
        }
    }


    @GetMapping("/{boardId}/{label}/{idList}")
    public List<Card> getIndex(@PathVariable String boardId, @PathVariable String label, @PathVariable String idList) {
        return getCardList(boardId, label, Optional.ofNullable(idList));

    }
}
