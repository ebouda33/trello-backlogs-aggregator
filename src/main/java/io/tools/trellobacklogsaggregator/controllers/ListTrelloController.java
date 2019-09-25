package io.tools.trellobacklogsaggregator.controllers;

import com.julienvey.trello.domain.TList;
import io.tools.trellobacklogsaggregator.configuration.CustomConfiguration;
import io.tools.trellobacklogsaggregator.service.TrelloApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lists")
public class ListTrelloController {

    @Autowired
    private TrelloApi trelloApi;

    @Autowired
    private CustomConfiguration customConfiguration;

    @GetMapping("/{idBoard}")
    public List<TList> getAll(@PathVariable String idBoard){
        return trelloApi.getBoardLists(idBoard);
    }

    @GetMapping("/default")
    public String getDefault(){
        return customConfiguration.getTrelloColumnInSprintByDefault();
    }
}
