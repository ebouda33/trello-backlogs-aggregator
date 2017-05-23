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

    public Double getCardValue(Card card) {
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

    public Double getComplexiteRealisee(Card card) {
        return getComplexite(card, "[", "]");
    }

    public Double getComplexiteTotale(Card card) {
        return getComplexite(card, "{", "}");
    }

    private Double getComplexite(Card card, String delimiterDebut, String delimiterFin) {
        Double value = 0.0;
        String cardName = card.getName().trim();
        if (cardName.contains(delimiterFin)) {
            int indexComplexiteDebut = cardName.lastIndexOf(delimiterDebut) + delimiterDebut.length();
            int indexComplexiteFin = cardName.lastIndexOf(delimiterFin);

            String complexitePattern = cardName.substring(indexComplexiteDebut, indexComplexiteFin);
            String complexite = complexitePattern.replace(",", ".");
            try {
                value = Double.valueOf(complexite);
            } catch (NumberFormatException e) {
                value = 0.0;
            }
        }
        return value;
    }
}
