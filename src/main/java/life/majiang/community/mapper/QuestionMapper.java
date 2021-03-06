package life.majiang.community.mapper;

import life.majiang.community.dto.QuestionDto;
import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface QuestionMapper {

    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmt_create},#{gmt_modified},#{creator},#{tag})")
    public void create(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param(value = "userId") Integer userId,@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(Integer userId);

    @Select("select * from question where id = #{id}")
    Question getById(@Param(value = "id")Integer id);

    @Update("update question set title = #{title},description = #{description},gmt_modified = #{gmt_modified},tag = #{tag} where id = #{id}")
    int update(Question question);

    @Update("update question set view_count = #{view_count}+1 where id=#{id}")
    void updateBySelect(Question updateQuestion);
}
