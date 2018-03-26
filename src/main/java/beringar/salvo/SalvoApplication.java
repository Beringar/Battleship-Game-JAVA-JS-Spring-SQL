package beringar.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }


    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepo, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, ShipRepository shipRepo, SalvoRepository salvoRepo, ScoreRepository scoreRepo) {
        return (String... args) -> {
            // save a couple of customers

            Player jBauer = new Player("j.bauer@ctu.gov", "24");
            Player cObrian = new Player("c.obrian@ctu.gov", "42");
            Player kBauer = new Player("kim_bauer@gmail.com", "kb");
            Player tAlmeida = new Player("t.almeida@ctu.gov", "mole");

            playerRepo.save(jBauer);
            playerRepo.save(cObrian);
            playerRepo.save(kBauer);
            playerRepo.save(tAlmeida);

            Game game1 = new Game();
            Game game2 = new Game();
            Game game3 = new Game();
            Game game4 = new Game();
            Game game5 = new Game();
            Game game6 = new Game();
            Game game7 = new Game();
            Game game8 = new Game();

            Date newGame2Date = Date.from(game1.getCreationDate().toInstant().plusSeconds(3600));

            game2.setCreationDate(newGame2Date);

            Date newGame3Date = Date.from(game1.getCreationDate().toInstant().plusSeconds(7200));

            game3.setCreationDate(newGame3Date);

            gameRepo.save(game1);
            gameRepo.save(game2);
            gameRepo.save(game3);
            gameRepo.save(game4);
            gameRepo.save(game5);
            gameRepo.save(game6);
            gameRepo.save(game7);
            gameRepo.save(game8);

            GamePlayer gameplayer1 = new GamePlayer(game1, jBauer);
            GamePlayer gameplayer2 = new GamePlayer(game1, cObrian);
            GamePlayer gameplayer3 = new GamePlayer(game2, jBauer);
            GamePlayer gameplayer4 = new GamePlayer(game2, cObrian);
            GamePlayer gameplayer5 = new GamePlayer(game3, cObrian);
            GamePlayer gameplayer6 = new GamePlayer(game3, tAlmeida);
            GamePlayer gameplayer7 = new GamePlayer(game4, cObrian);
            GamePlayer gameplayer8 = new GamePlayer(game4, jBauer);
            GamePlayer gameplayer9 = new GamePlayer(game5, tAlmeida);
            GamePlayer gameplayer10 = new GamePlayer(game5, jBauer);
            GamePlayer gameplayer11 = new GamePlayer(game6, kBauer);
            GamePlayer gameplayer12 = new GamePlayer(game7, tAlmeida);
            GamePlayer gameplayer13 = new GamePlayer(game8, kBauer);
            GamePlayer gameplayer14 = new GamePlayer(game8, tAlmeida);



            gamePlayerRepo.save(gameplayer1);
            gamePlayerRepo.save(gameplayer2);
            gamePlayerRepo.save(gameplayer3);
            gamePlayerRepo.save(gameplayer4);
            gamePlayerRepo.save(gameplayer5);
            gamePlayerRepo.save(gameplayer6);
            gamePlayerRepo.save(gameplayer7);
            gamePlayerRepo.save(gameplayer8);
            gamePlayerRepo.save(gameplayer9);
            gamePlayerRepo.save(gameplayer10);
            gamePlayerRepo.save(gameplayer11);
            gamePlayerRepo.save(gameplayer12);
            gamePlayerRepo.save(gameplayer13);
            gamePlayerRepo.save(gameplayer14);

            List<String> ship1l = new ArrayList<>(Arrays.asList("E1","F1","G1"));
            Ship ship1 = new Ship("Submarine", gameplayer1, ship1l);
            shipRepo.save(ship1);
            List<String> ship2l = new ArrayList<>(Arrays.asList("B4", "B5"));
            Ship ship2 = new Ship("Patrol Boat", gameplayer1, ship2l);
            shipRepo.save(ship2);
            List<String> ship3l = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
            Ship ship3 = new Ship("Destroyer", gameplayer2, ship3l);
            shipRepo.save(ship3);
            List<String> ship4l = new ArrayList<>(Arrays.asList("F1", "F2"));
            Ship ship4 = new Ship("Patrol Boat", gameplayer2, ship4l);
            shipRepo.save(ship4);
            List<String> ship5l = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
            Ship ship5 = new Ship("Destroyer", gameplayer3, ship5l);
            shipRepo.save(ship5);
            List<String> ship6l = new ArrayList<>(Arrays.asList("C6", "C7"));
            Ship ship6 = new Ship("Patrol Boat", gameplayer3, ship6l);
            shipRepo.save(ship6);
            List<String> ship7l = new ArrayList<>(Arrays.asList("A2", "A3", "A4"));
            Ship ship7 = new Ship("Submarine", gameplayer4, ship7l);
            shipRepo.save(ship7);
            List<String> ship8l = new ArrayList<>(Arrays.asList("G6", "H6"));
            Ship ship8 = new Ship("Patrol Boat", gameplayer4, ship8l);
            shipRepo.save(ship8);
            List<String> ship9l = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
            Ship ship9 = new Ship("Destroyer", gameplayer5, ship9l);
            shipRepo.save(ship9);
            List<String> ship10l = new ArrayList<>(Arrays.asList("C6", "C7"));
            Ship ship10 = new Ship("Patrol Boat", gameplayer5, ship10l);
            shipRepo.save(ship10);
            List<String> ship11l = new ArrayList<>(Arrays.asList("A2", "A3", "A4"));
            Ship ship11 = new Ship("Submarine", gameplayer6, ship11l);
            shipRepo.save(ship11);
            List<String> ship12l = new ArrayList<>(Arrays.asList("G6", "H6"));
            Ship ship12 = new Ship("Patrol Boat", gameplayer6, ship12l);
            shipRepo.save(ship12);
            List<String> ship13l = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
            Ship ship13 = new Ship("Destroyer", gameplayer7, ship13l);
            shipRepo.save(ship13);
            List<String> ship14l = new ArrayList<>(Arrays.asList("C6", "C7"));
            Ship ship14 = new Ship("Patrol Boat", gameplayer7, ship14l);
            shipRepo.save(ship14);
            List<String> ship15l = new ArrayList<>(Arrays.asList("A2", "A3", "A4"));
            Ship ship15 = new Ship("Submarine", gameplayer8, ship15l);
            shipRepo.save(ship15);
            List<String> ship16l = new ArrayList<>(Arrays.asList("G6", "H6"));
            Ship ship16 = new Ship("Patrol Boat", gameplayer8, ship16l);
            shipRepo.save(ship16);
            List<String> ship17l = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
            Ship ship17 = new Ship("Destroyer", gameplayer9, ship17l);
            shipRepo.save(ship17);
            List<String> ship18l = new ArrayList<>(Arrays.asList("C6", "C7"));
            Ship ship18 = new Ship("Patrol Boat", gameplayer9, ship18l);
            shipRepo.save(ship18);
            List<String> ship19l = new ArrayList<>(Arrays.asList("A2", "A3", "A4"));
            Ship ship19 = new Ship("Submarine", gameplayer10, ship19l);
            shipRepo.save(ship19);
            List<String> ship20l = new ArrayList<>(Arrays.asList("G6", "H6"));
            Ship ship20 = new Ship("Patrol Boat", gameplayer10, ship20l);
            shipRepo.save(ship20);
            List<String> ship21l = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
            Ship ship21 = new Ship("Destroyer", gameplayer11, ship21l);
            shipRepo.save(ship21);
            List<String> ship22l = new ArrayList<>(Arrays.asList("C6", "C7"));
            Ship ship22 = new Ship("Patrol Boat", gameplayer11, ship22l);
            shipRepo.save(ship22);
            List<String> ship23l = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
            Ship ship23 = new Ship("Destroyer", gameplayer13, ship23l);
            shipRepo.save(ship23);
            List<String> ship24l = new ArrayList<>(Arrays.asList("C6", "C7"));
            Ship ship24 = new Ship("Patrol Boat", gameplayer13, ship24l);
            shipRepo.save(ship24);
            List<String> ship25l = new ArrayList<>(Arrays.asList("A2", "A3", "A4"));
            Ship ship25 = new Ship("Submarine", gameplayer14, ship25l);
            shipRepo.save(ship25);
            List<String> ship26l = new ArrayList<>(Arrays.asList("G6", "H6"));
            Ship ship26 = new Ship("Patrol Boat", gameplayer14, ship26l);
            shipRepo.save(ship26);

            Salvo salvo1 = new Salvo(gameplayer1, 1, new ArrayList<>(Arrays.asList("B5", "C5", "F1")));
            salvoRepo.save(salvo1);
            Salvo salvo2 = new Salvo(gameplayer2, 1, new ArrayList<>(Arrays.asList("B4", "B5", "B6")));
            salvoRepo.save(salvo2);
            Salvo salvo3 = new Salvo(gameplayer1, 2, new ArrayList<>(Arrays.asList("F2", "D5")));
            salvoRepo.save(salvo3);
            Salvo salvo4 = new Salvo(gameplayer2, 2, new ArrayList<>(Arrays.asList("E1", "H3", "A2")));
            salvoRepo.save(salvo4);
            Salvo salvo5 = new Salvo(gameplayer3, 1, new ArrayList<>(Arrays.asList("A2", "A4", "G6")));
            salvoRepo.save(salvo5);
            Salvo salvo6 = new Salvo(gameplayer4, 1, new ArrayList<>(Arrays.asList("B5", "D5", "C7")));
            salvoRepo.save(salvo6);
            Salvo salvo7 = new Salvo(gameplayer3, 2, new ArrayList<>(Arrays.asList("A3", "H6")));
            salvoRepo.save(salvo7);
            Salvo salvo8 = new Salvo(gameplayer4, 2, new ArrayList<>(Arrays.asList("C5", "C6")));
            salvoRepo.save(salvo8);
            Salvo salvo9 = new Salvo(gameplayer5, 1, new ArrayList<>(Arrays.asList("G6", "H6", "A4")));
            salvoRepo.save(salvo9);
            Salvo salvo10 = new Salvo(gameplayer6, 1, new ArrayList<>(Arrays.asList("H1", "H2", "H3")));
            salvoRepo.save(salvo10);
            Salvo salvo11 = new Salvo(gameplayer5, 2, new ArrayList<>(Arrays.asList("A2", "A3", "D8")));
            salvoRepo.save(salvo11);
            Salvo salvo12 = new Salvo(gameplayer6, 2, new ArrayList<>(Arrays.asList("E1", "F2", "G3")));
            salvoRepo.save(salvo12);
            Salvo salvo13 = new Salvo(gameplayer7, 1, new ArrayList<>(Arrays.asList("A3", "A4", "F7")));
            salvoRepo.save(salvo13);
            Salvo salvo14 = new Salvo(gameplayer8, 1, new ArrayList<>(Arrays.asList("B5", "C6", "H1")));
            salvoRepo.save(salvo14);
            Salvo salvo15 = new Salvo(gameplayer7, 2, new ArrayList<>(Arrays.asList("A2", "G6", "H6")));
            salvoRepo.save(salvo15);
            Salvo salvo16 = new Salvo(gameplayer8, 2, new ArrayList<>(Arrays.asList("C5", "C7", "D5")));
            salvoRepo.save(salvo16);
            Salvo salvo17 = new Salvo(gameplayer9, 1, new ArrayList<>(Arrays.asList("A1", "A2", "A3")));
            salvoRepo.save(salvo17);
            Salvo salvo18 = new Salvo(gameplayer10, 1, new ArrayList<>(Arrays.asList("B5", "B6", "C7")));
            salvoRepo.save(salvo18);
            Salvo salvo19 = new Salvo(gameplayer9, 2, new ArrayList<>(Arrays.asList("G6", "G7", "G8")));
            salvoRepo.save(salvo19);
            Salvo salvo20 = new Salvo(gameplayer10, 2, new ArrayList<>(Arrays.asList("C6", "D6", "E6")));
            salvoRepo.save(salvo20);
            Salvo salvo21 = new Salvo(gameplayer10, 3, new ArrayList<>(Arrays.asList("H1", "H8")));
            salvoRepo.save(salvo21);

            Score score1 = new Score(game1, jBauer, 1.0);
            scoreRepo.save(score1);
            Score score2 = new Score(game1, cObrian, 0.0);
            scoreRepo.save(score2);
            Score score3 = new Score(game2, jBauer, 0.5);
            scoreRepo.save(score3);
            Score score4 = new Score(game2, cObrian, 0.5);
            scoreRepo.save(score4);
            Score score5 = new Score(game3, cObrian, 1.0);
            scoreRepo.save(score5);
            Score score6 = new Score(game3, tAlmeida, 0.0);
            scoreRepo.save(score6);
            Score score7 = new Score(game4, cObrian, 0.5);
            scoreRepo.save(score7);
            Score score8 = new Score(game4, jBauer, 0.5);
            scoreRepo.save(score8);

            System.out.println("Hello Salvo Beringar!");
        };
    }
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName -> {
            Player player = playerRepository.findByEmail(inputName);
            if (player != null) {
                return new User(player.getEmail(), player.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }

}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // .antMatchers("/admin/**").hasAuthority("ADMIN")
                //  .antMatchers("/**").hasAuthority("USER")
                //  .antMatchers("/**").denyAll()
                .antMatchers("/web/games.html").permitAll()
                .anyRequest().fullyAuthenticated();

        http.formLogin()
                .usernameParameter("name")
                .passwordParameter("pwd")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}










