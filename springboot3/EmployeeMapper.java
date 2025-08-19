@Mapper
public interface EmployeeMapper {
    
    @Delete("delete from `employee` where id = #{id}")
    void deleteById(Integer id);
    
    // 其他方法...
}