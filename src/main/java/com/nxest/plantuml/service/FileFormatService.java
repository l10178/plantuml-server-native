package com.nxest.plantuml.service;


import com.nxest.plantuml.config.PlantumlServerConfig;
import net.sourceforge.plantuml.FileFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import java.util.Map;
import java.util.Optional;

@Service
public class FileFormatService {

    private final PlantumlServerConfig config;

    private static final Map<String, FileFormat> supportFormats = Map.of(
            "png", FileFormat.PNG,
            "img", FileFormat.PNG,
            "base64", FileFormat.BASE64,
            "svg", FileFormat.SVG,
            "eps", FileFormat.EPS,
            "epstext", FileFormat.EPS_TEXT,
            "txt", FileFormat.UTXT,
            "map", FileFormat.UTXT
    );

    @Autowired
    public FileFormatService(PlantumlServerConfig config) {
        this.config = config;
    }

    public FileFormat getOrDefaultFileFormat(String format) {
        return Optional.ofNullable(format)
                .filter(s -> !s.isBlank())
                .or(() -> Optional.of(config.getDefaultFileFormat()))
                .map(supportFormats::get)
                .orElseThrow();
    }

    public MediaType asMediaType(FileFormat format) {
        return MediaType.asMediaType(MimeType.valueOf(format.getMimeType()));
    }

}
