package io.tools.trellobacklogsaggregator.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julienvey.trello.domain.TList;

import io.tools.trellobacklogsaggregator.configuration.CustomConfiguration;

@Service
public class ListService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomConfiguration customConfiruration;

    public void checkListConsistency(List<TList> tlists) throws Exception {
        Map<String, Boolean> columnExistency = new HashMap<>();

        for (TList tList : tlists) {
            String listName = tList.getName().toLowerCase();
            if (!customConfiruration.getColumnAllowed().contains(listName)) {
                logger.error("The list " + listName + " is not allowed by the configuration");
                throw new Exception("The list " + listName + " is not allowed by the configuration");
            }
            columnExistency.put(listName, true);
        }

        for (String columnAllowed : customConfiruration.getColumnAllowed()) {
            if (columnExistency.get(columnAllowed) == null
                    || columnExistency.get(columnAllowed).equals(Boolean.FALSE)) {
                logger.error(columnAllowed + " should be in Trello but it does not exist");
                throw new Exception(columnAllowed + " should be in Trello but it does not exist");
            }
        }
    }

    public boolean checkListAllowed(TList tlist, List<String> listNameAllowed) {
        return listNameAllowed.contains(tlist.getName().toLowerCase());
    }

}
