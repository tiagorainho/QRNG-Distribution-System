package qrng.QrngService.Generator;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generator")
public class GeneratorController {

    @Autowired
    private final GeneratorService generatorService;

    public GeneratorController(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Map<String, String>> registerGenerator(
        HttpServletRequest request,
        @RequestBody Generator generator
    ) {
        generatorService.registry(
            generator.getName(),
            (generator.getUrl() == null) ? request.getRemoteAddr(): generator.getUrl(),
            generator.getType());

        return new ResponseEntity<>(
            Map.of("success", "Generator '" + generator.getName() + "' created with success"),
            HttpStatus.CREATED
        );
    }

    @RequestMapping(value="", method=RequestMethod.GET)
    public ResponseEntity<Map<String, List<Generator>>> getGeneratorsName() {
        return new ResponseEntity<>(
            Map.of("generators", generatorService.generators()),
            HttpStatus.OK
        );
    }
}
