package life.majiang.community.service;

import life.majiang.community.dto.PaginationDto;
import life.majiang.community.dto.QuestionDto;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
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

        PaginationDto paginationDto = new PaginationDto();
        Integer totalPage;
        Integer totalCount = questionMapper.count();
        if (totalCount%size ==0){
            totalPage=totalCount/size;
        }else {
            totalPage=totalCount/size+1;
        }
        if (page<1){
            page = 1;
        }
        if (page > totalPage){
            page = totalPage;
        }
        paginationDto.setPagination(totalPage,page);
        //offset=size*(page-1)
        Integer offset = size*(page-1);
        List<Question> list = questionMapper.list(offset,size);
        List<QuestionDto> questionDtoList = new ArrayList<>();

        for (Question question : list) {
            User user = userMapper.findById(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDto.setQuestionDtos(questionDtoList);

        return paginationDto;
    }

    public PaginationDto list(Integer userId, Integer page, Integer size) {
        Integer offset = size*(page-1);
        List<Question> list = questionMapper.listByUserId(userId,offset,size);

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
        Integer totalPage;
        Integer totalCount = questionMapper.countByUserId(userId);
        if (totalCount%size ==0){
            totalPage=totalCount/size;
        }else {
            totalPage=totalCount/size+1;
        }
        if (page<1){
            page = 1;
        }
        if (page > totalPage){
            page = totalPage;
        }
        paginationDto.setPagination(totalPage,page);
        return paginationDto;
    }

    public QuestionDto getById(Integer id) {
        Question question = questionMapper.getById(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question,questionDto);
        User user = userMapper.findById(question.getCreator());
        questionDto.setUser(user);
        return questionDto;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null){
            //创建
            question.setGmt_create(System.currentTimeMillis());
            question.setGmt_modified(question.getGmt_create());
            questionMapper.create(question);
        }else {
            //更新
            question.setGmt_modified(System.currentTimeMillis());
            int update = questionMapper.update(question);
            if (update != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Integer id) {
        Question question = questionMapper.getById(id);
        questionMapper.updateBySelect(question);
    }
}
