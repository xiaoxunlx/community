package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDto;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id")Integer id,
                       Model model){
        QuestionDto question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        return "publish";
    }

    @RequestMapping("/publish")
    public String publish(){
        return "publish";
    }
    @PostMapping("/publish")
    public String doPublish(@RequestParam("title")String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            @RequestParam("id")Integer id,
                            HttpServletRequest request,
                            Model model){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            model.addAttribute("error","用户未登录！");
            return "publish";
        }
        /**
         * 会写数据到页面
         */
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if(title == null || title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description == null || description==""){
            model.addAttribute("error","内容不能为空");
            return "publish";
        }
        if(tag == null || tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());

        question.setId(id);
        questionService.createOrUpdate(question);
//        questionMapper.create(question);
        return "redirect:/";
    }
}
