package es.tucesta.dao;

import es.tucesta.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;


public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query(nativeQuery = true, value= "SELECT * FROM tbl_recipe WHERE name LIKE CONCAT('%',:search,'%')")
    List<Recipe> findByNameContaining(String search);

    @Query(nativeQuery = true, value= "select DISTINCTROW tbl_recipe.* FROM tbl_recipe left join tbl_recipe_cat on tbl_recipe.id=tbl_recipe_cat.recipe_id where tbl_recipe_cat.cat_id in (:categories)")
    List<Recipe> findByCategory(List<String> categories);

}