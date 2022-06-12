package jhaman.das.challangeubitricity.controllers;

import io.swagger.annotations.*;
import jhaman.das.challangeubitricity.services.EvUbiService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Api(tags = "EV Ubi Controller")
@RestController
@RequestMapping("v1/evubi")
@AllArgsConstructor
public class EvUbiController {
    private final EvUbiService _evUbiService;

    @PostMapping("/plug-car/{number}")
    @ApiOperation(value = "plug vehicle at station number given in the path")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Server Exception")
    })
    public ResponseEntity<Object> plugCar(@PathVariable @ApiParam("enter car id between 1 to 10 greater than that not acceptable") int number) {
        validateRequest(number);
        _evUbiService.add(number);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/plug-car/{number}")
    @ApiOperation(value = "unplug a vehicle at station number given in the path")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Server Exception")
    })
    public ResponseEntity<Object> removeCar(@PathVariable @ApiParam("enter a value between 1 to 10") int number) {
        validateRequest(number);
        _evUbiService.delete(number);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/getUbiStatus")
    @ApiOperation(value = "get the status of EvUbi")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Server Exception")
    })
    public ResponseEntity<Map<Integer, String>> getUbiStatus() {
        return ResponseEntity.ok(_evUbiService.getUbiStatus());
    }

    private void validateRequest(@PathVariable @ApiParam("enter car id between 1 to 10 greater than that not acceptable") int number) {
        if (number < 1 || number > 10)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Number is not available");
    }

}
