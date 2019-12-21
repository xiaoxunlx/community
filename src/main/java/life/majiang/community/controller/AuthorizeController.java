package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDto;
import life.majiang.community.dto.GitHubUser;
import life.majiang.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @Value("${github.client.id}")
    private String clientID;
    @Value("${github.client.secret}")
    private String clientsecert;
    @Value("${github.redirect.uri}")
    private String redirecturi;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                            HttpServletRequest request){

        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(clientID);
        accessTokenDto.setClient_secret(clientsecert);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(redirecturi);
        accessTokenDto.setState(state);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDto);
        GitHubUser gitHubUser = gitHubProvider.getUser(accessToken);
//        System.out.println(gitHubUser.getName());
        if (gitHubUser!=null){
            //登陆成功，写cookie和session
            request.getSession().setAttribute("user",gitHubUser);
            return "redirect:/";
        }else {
            //登陆失败
            return "redirect:/";
        }
    }
}
