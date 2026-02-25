package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> entity = new RestTemplate()
                .getForEntity("http://localhost:8080/animes/10", Anime.class);
        log.info(entity);

        Anime[] animes = new RestTemplate()
                .getForObject("http://localhost:8080/animes/all", Anime[].class);
        log.info(Arrays.toString(animes));

        ResponseEntity<List<Anime>> exchange = new RestTemplate()
                .exchange("http://localhost:8080/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                });
        log.info(exchange.getBody());

//        Anime kingdom = Anime.builder().name("Kingdom").build();
//        Anime kingdomSaved = new RestTemplate().postForObject("http://localhost:8080/animes", kingdom, Anime.class);
//        log.info("Saved anime {}",kingdomSaved);

        Anime samuraiChamploo = Anime.builder().name("Samurai Champloo").build();
        ResponseEntity<Anime> samuraiChamplooSaved =
                new RestTemplate().exchange("http://localhost:8080/animes",HttpMethod.POST, new HttpEntity<>(samuraiChamploo), Anime.class);
        log.info("Saved anime {}",samuraiChamplooSaved);

        Anime animeTobEUpdated = samuraiChamplooSaved.getBody();
        animeTobEUpdated.setName("SamuraiChamploo 2");

        ResponseEntity<Void> samuraiChamplooUpdated =
                new RestTemplate().exchange("http://localhost:8080/animes",HttpMethod.PUT, new HttpEntity<>(animeTobEUpdated), Void.class);
        log.info("Saved updated {}",samuraiChamplooUpdated);

        ResponseEntity<Void> samuraiChamplooDeleted =
                new RestTemplate().exchange("http://localhost:8080/animes/{id}",HttpMethod.DELETE, null,Void.class,animeTobEUpdated.getId());
        log.info("Saved deleted {}",samuraiChamplooDeleted);


    }
}
