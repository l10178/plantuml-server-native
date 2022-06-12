package com.nxest.plantuml.service;

import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.syntax.LanguageDescriptor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.StringJoiner;

@Service
public class LanguageService {

    public ResponseEntity<String> printLanguages() {
        LanguageDescriptor desc = new LanguageDescriptor();
        Set<String> type = desc.getType();
        Set<String> keyword = desc.getKeyword();
        Set<String> preprocessor = desc.getPreproc();
        Collection<String> skin = SkinParam.getPossibleValues();
        Collection<String> color = HColorSet.instance().names();

        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        // add `type`
        joiner.add(";type").add(";" + type.size());
        type.forEach(joiner::add);
        //join a new line
        joiner.add("");

        // add `keyword`
        joiner.add(";keyword").add(";" + keyword.size());
        keyword.forEach(joiner::add);
        joiner.add("");

        // add `preprocessor`
        joiner.add(";preprocessor").add(";" + preprocessor.size());
        preprocessor.forEach(joiner::add);
        joiner.add("");

        // add `skinparameter`
        joiner.add(";skinparameter").add(";" + skin.size());
        skin.forEach(joiner::add);
        joiner.add("");

        // add `color`
        joiner.add(";color").add(";" + color.size());
        color.forEach(joiner::add);
        joiner.add("");

        // add EOF
        joiner.add(";EOF");

        return ResponseEntity.ok(joiner.toString());
    }
}
