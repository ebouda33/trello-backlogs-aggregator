package io.tools.trellobacklogsaggregator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.TList;
import io.tools.trellobacklogsaggregator.configuration.CustomConfiguration;
import io.tools.trellobacklogsaggregator.execptions.ListException;
import io.tools.trellobacklogsaggregator.model.BacklogsData;
import io.tools.trellobacklogsaggregator.model.BoardDetail;
import io.tools.trellobacklogsaggregator.model.CardModel;
import io.tools.trellobacklogsaggregator.repository.BacklogsRepository;
import io.tools.trellobacklogsaggregator.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Member;

import io.tools.trellobacklogsaggregator.model.CardWithMembers;

@Service
public class CardService {

    private BacklogsRepository backlogsRepository;
    private CustomConfiguration configurationProperties;
    private CardRepository cardRepository;
    private Trello trello;


    @Autowired
    public CardService(BacklogsRepository backlogsRepository,CardRepository cardRepository, CustomConfiguration configurationProperties, Trello trelloService) {
        this.backlogsRepository = backlogsRepository;
        this.configurationProperties = configurationProperties;
        this.trello = trelloService;
        this.cardRepository = cardRepository;
    }


    public String getCardName(Card card) {
        String cardName = card.getName().trim().replaceAll("[\\t\\n\\r]", " ").replaceAll(" +", " ");
        String[] nameElements = cardName.split(" ");
        StringBuilder reference = new StringBuilder();
        for (String nameElement : nameElements) {
            if ('(' != nameElement.charAt(0) && '[' != nameElement.charAt(0) && '{' != nameElement.charAt(0)) {
                reference.append(nameElement);
                reference.append(" ");
            }
        }
        return reference.toString().trim();
    }

    public Double getBusinessComplexity(Card card) {
        Double value = 0.0;
        String cardName = card.getName().trim();
        if ('(' == cardName.charAt(0)) {
            int endIndex = cardName.indexOf(")");
            String valueStr = cardName.substring(1, endIndex);
            valueStr = valueStr.replace(",", ".");
            value = Double.valueOf(valueStr);
        }
        return value;
    }

    public Double getConsumedComplexity(Card card) {
        return getComplexity(card, "[", "]");
    }

    public Double getTotalComplexity(Card card) {
        return getComplexity(card, "{", "}");
    }

    private Double getComplexity(Card card, String delimiterStart, String delimiterEnd) {
        Double value = 0.0;
        String cardName = card.getName().trim();
        if (cardName.contains(delimiterEnd)) {
            int indexComplexityStart = cardName.lastIndexOf(delimiterStart) + delimiterStart.length();
            int indexComplexityEnd = cardName.lastIndexOf(delimiterEnd);

            String complexityPattern = cardName.substring(indexComplexityStart, indexComplexityEnd);
            String complexity = complexityPattern.replace(",", ".");
            try {
                value = Double.valueOf(complexity);
            } catch (NumberFormatException e) {
                value = 0.0;
            }
        }
        return value;
    }

    public void setConsumedComplexity(Card card, Double time) {
        setComplexity(card, time, "[", "]");
    }

    public void setTotalComplexity(Card card, Double time) {
        setComplexity(card, time, "{", "}");
    }
    public void setBusinessComplexity(Card card, Double time) {
        setComplexity(card, time, "(", ")");
    }

    public void setComplexity(Card card, Double time, String delimiterStart, String delimiterEnd) {
        Pattern p = Pattern.compile("(\\[(\\d+(.\\d+)?)\\])");
        String name = card.getName();
        Matcher m = p.matcher(name);
        boolean find = false;
        while (m.find()) {
            name = m.replaceAll("[" + time + "]");
            find = true;
        }
        if (!find) {
            name += " [" + time + "]";
        }
        card.setName(name);
        trello.updateCard(card);
    }

    public CardWithMembers createCardWithMembers(Card card, Map<String, Member> possibleMembers, String backlogName) {
        List<Member> cardMembers = new ArrayList<>();
        card.getIdMembers().forEach(idMember -> {
            cardMembers.add(possibleMembers.get(idMember));
        });
        return new CardWithMembers(card, cardMembers, backlogName);
    }

    public List<Card> getCardsFromListsByLabel(String idBoard, String label) {
        final BacklogsData read = backlogsRepository.read();
        if (read == null) {
            throw new RuntimeException("Batch is not started");
        }
        final BoardDetail board = read.getBoard(idBoard);
//        configurationProperties.getColumnInSprintAllowed()

        return board.getListByLabel(label).orElseThrow(ListException::new).getCardList();

    }

    public CardModel findAndSave(CardModel card) {
        return cardRepository.save(card);
    }
}
