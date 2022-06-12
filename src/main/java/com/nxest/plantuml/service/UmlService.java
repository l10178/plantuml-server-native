package com.nxest.plantuml.service;

import com.nxest.plantuml.exception.PlantumlException;
import net.sourceforge.plantuml.*;
import net.sourceforge.plantuml.code.Base64Coder;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.error.PSystemError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static com.nxest.plantuml.config.Constants.DEFAULT_ERROR_RESPONSE;

/**
 * Uml diagram render service.
 */
@Service
public class UmlService {

    private static final Logger log = LoggerFactory.getLogger(UmlService.class);

    private final FileFormatService fileFormatService;
    private final TranscoderService transcoderService;

    @Autowired
    public UmlService(FileFormatService fileFormatService, TranscoderService transcoderService) {
        this.fileFormatService = fileFormatService;
        this.transcoderService = transcoderService;
    }

    public ResponseEntity<byte[]> renderByEncodedUrl(String encoded, String fmt, Integer index) {
        String uml = getUmlSource(encoded);
        return renderByUml(uml, fileFormatService.getOrDefaultFileFormat(fmt), index);
    }

    public ResponseEntity<byte[]> renderByUml(String umlSource, String fmt, Integer index) {
        return renderByUml(umlSource, fileFormatService.getOrDefaultFileFormat(fmt), index);
    }

    public ResponseEntity<byte[]> renderByUml(String umlSource, FileFormat format, Integer index) {
        index = Optional.ofNullable(index).orElse(0);

        SourceStringReader reader = new SourceStringReader(umlSource);
        List<BlockUml> blocks = reader.getBlocks();
        BlockUml block = blocks.get(index);
        Diagram diagram = block.getDiagram();
        if (diagram instanceof PSystemError) {
            return DEFAULT_ERROR_RESPONSE;
        }

        MediaType mediaType = fileFormatService.asMediaType(format);
        // close output
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            diagram.exportDiagram(out, 0, new FileFormatOption(format));

            if (FileFormat.BASE64 == format) {
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(base64Image(out));
            }
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(out.toByteArray());
        } catch (IOException e) {
            log.error("export diagram error.", e);
            return DEFAULT_ERROR_RESPONSE;
        }
    }

    private byte[] base64Image(ByteArrayOutputStream out) {
        String base64 = Base64Coder.encodeLines(out.toByteArray()).replaceAll("\\s", "");
        String encodedBytes = "data:image/png;base64," + base64;
        return encodedBytes.getBytes();
    }

    public String getUmlSource(String encoded) {
        // build the UML source from the compressed part of the URL
        String text = URLDecoder.decode(encoded, StandardCharsets.UTF_8);
        try {
            text = transcoderService.decode(text);
        } catch (PlantumlException e) {
            log.warn("decode error.", e);
            text = "' unable to decode string";
        }

        // encapsulate the UML syntax if necessary
        if (text.startsWith("@start")) {
            return text;
        }
        // return error tips uml
        return """
                @startuml
                %s
                @enduml
                """.formatted(text);
    }

    public ResponseEntity<byte[]> renderMap(String encoded, Integer index) {
        String uml = getUmlSource(encoded);
        int idx = Optional.ofNullable(index).filter(x -> x > 0).orElse(0);
        String map = extractMap(uml, idx);
        return Optional.ofNullable(map)
                .map(s -> ResponseEntity.ok()
                        .contentType(fileFormatService.asMediaType(FileFormat.UTXT))
                        .body(s.getBytes(StandardCharsets.UTF_8)))
                .orElse(DEFAULT_ERROR_RESPONSE);
    }


    public String extractMap(String uml, int idx) {
        SourceStringReader reader = new SourceStringReader(uml);
        BlockUml blockUml = reader.getBlocks().get(idx);
        final Diagram diagram = blockUml.getDiagram();
        try {
            ImageData map = diagram.exportDiagram(new NullOutputStream(), 0,
                    new FileFormatOption(FileFormat.PNG, false));
            if (map.containsCMapData()) {
                return map.getCMapData("plantuml");
            }
        } catch (IOException e) {
            log.warn("Export diagram error.", e);
        }
        return null;
    }

    public ResponseEntity<byte[]> sendCheck(String encoded) {
        String uml = getUmlSource(encoded);
        SourceStringReader reader = new SourceStringReader(uml);
        try {
            DiagramDescription desc = reader.outputImage(
                    new NullOutputStream(),
                    new FileFormatOption(FileFormat.PNG, false)
            );
            return ResponseEntity.ok()
                    .contentType(fileFormatService.asMediaType(FileFormat.UTXT))
                    .body(desc.getDescription().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.warn("Export diagram error.", e);
            return DEFAULT_ERROR_RESPONSE;
        }
    }
}
