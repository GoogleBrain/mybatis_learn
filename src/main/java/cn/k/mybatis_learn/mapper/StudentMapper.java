package cn.k.mybatis_learn.mapper;

import java.util.HashMap;
import java.util.List;

import cn.k.mybatis_learn.domain.StudentDomain;
import org.apache.ibatis.annotations.Param;

public interface StudentMapper {

	List<StudentDomain> queryById(StudentDomain studentDomain);

	List<StudentDomain> queryByIdbydaole(StudentDomain studentDomain);
	
	void insertkkk(StudentDomain sd);

	void insertTrim(StudentDomain sd);

	void insertList(List<StudentDomain> list);

	void update(StudentDomain studentDomain);

	List<StudentDomain> queryByIdForeach(List<Integer> lists);

	List<StudentDomain>queryByIdForeach2(Integer[] array);

	List<StudentDomain>queryByIdForeach3(@Param("enk")HashMap<String,Integer> entri);

	HashMap<String,Object> selectresultmap();
}
