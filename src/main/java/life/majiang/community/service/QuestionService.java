package life.majiang.community.service;

import life.majiang.community.dto.PaginationDto;
import life.majiang.community.dto.QuestionDto;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDto list(Integer page, Integer size) {
        //offset=size*(page-1)
        Integer offset = size*(page-1);
        List<Question> list = questionMapper.list(offset,size);
        List<QuestionDto> questionDtoList = new ArrayList<>();
        PaginationDto paginationDto = new PaginationDto();
        for (Question question : list) {
            User user = userMapper.findById(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDto.setQuestionDtos(questionDtoList);
        Integer totalCount = questionMapper.count();
        paginationDto.setPagination(totalCount,page,size);
        return paginationDto;
    }
}
