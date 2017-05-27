package io.tools.trellobacklogsaggregator.service;

import org.springframework.stereotype.Service;

import com.julienvey.trello.domain.Card;

@Service
public class CardService {
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
}
