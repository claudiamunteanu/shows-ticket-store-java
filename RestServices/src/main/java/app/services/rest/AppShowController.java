package app.services.rest;

import app.model.Show;
import app.persistence.RepositoryException;
import app.persistence.interfaces.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/shows")
@CrossOrigin(origins = "http://localhost:3000")
public class AppShowController {
    private static final String template = "Hello, %s!";

    @Autowired
    private ShowRepository showRepository;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name){
        return String.format(template, name);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Show> getAll(){
        return showRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id){
        Show show = showRepository.findOne(Long.parseLong(id));
        if(show == null)
            return new ResponseEntity<String>("Show not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Show>(show, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Show create(@RequestBody Show show){
        showRepository.save(show);
        return show;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody Show show){
        System.out.println("Updating user...");
        showRepository.update(show.getId(), show);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String id){
        System.out.println("Deleting user..." + id);
        try{
            showRepository.delete(Long.parseLong(id));
            return new ResponseEntity<Show>(HttpStatus.OK);
        } catch (Exception e){
            System.out.println("Ctrl Delete show exception");
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String showError(RepositoryException e){
        return e.getMessage();
    }

}
