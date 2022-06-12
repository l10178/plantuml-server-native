package com.nxest.plantuml.service;

import com.nxest.plantuml.exception.PlantumlException;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TranscoderService {

    private static final Transcoder coder = TranscoderUtil.getDefaultTranscoder();

    /**
     * URL regex pattern to easily extract index and encoded diagram.
     */
    private static final Pattern URL_PATTERN = Pattern.compile(
            "/\\w+(?:/(?<idx>\\d+))?(?:/(?<encoded>[^/]+))?/?$"
    );

    public String encode(String text) {
        try {
            return coder.encode(text);
        } catch (IOException e) {
            throw new PlantumlException(e);
        }
    }

    public String decode(String code) {
        try {
            return coder.decode(code);
        } catch (NoPlantumlCompressionException e) {
            throw new PlantumlException(e);
        }
    }

    /**
     * Get encoded diagram source from URL.
     *
     * @param url URL to analyse, e.g., returned by `request.getRequestURI()`
     * @return if exists diagram index; otherwise `null`
     */
    public String getEncodedDiagram(final String url) {
        return getEncodedDiagram(url, null);
    }

    /**
     * Get encoded diagram source from URL.
     *
     * @param url      URL to analyse, e.g., returned by `request.getRequestURI()`
     * @param fallback fallback if no encoded diagram source exists in {@code url}
     * @return if exists diagram index; otherwise {@code fallback}
     */
    public String getEncodedDiagram(final String url, final String fallback) {
        Matcher matcher = URL_PATTERN.matcher(url);
        if (!matcher.find()) {
            return fallback;
        }
        String encoded = matcher.group("encoded");
        if (encoded == null) {
            return fallback;
        }
        return encoded;
    }
}
