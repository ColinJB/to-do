import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;

public class CategoryTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/to_do_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteTasksQuery = "DELETE FROM tasks *;";
      String deleteCategoriesQuery = "DELETE FROM categories *;";
      con.createQuery(deleteTasksQuery).executeUpdate();
      con.createQuery(deleteCategoriesQuery).executeUpdate();
    }
  }

  @Test
  public void category_instantiatesCorrectly_true() {
    Category testCategory = new Category("Home");
    assertEquals(true, testCategory instanceof Category);
  }

  @Test
  public void getName_categoryInstantiatesWithName_Home() {
    Category testCategory = new Category("Home");
    assertEquals("Home", testCategory.getName());
  }

  @Test
  public void all_returnsAllInstancesOfCategory_true() {
    Category firstCategory = new Category("Home");
    firstCategory.save();
    Category secondCategory = new Category("Work");
    secondCategory.save();
    assertEquals(true, Category.all().get(0).equals(firstCategory));
    assertEquals(true, Category.all().get(1).equals(secondCategory));
  }

  @Test
  public void getId_categoriesInstantiateWithAnId_1() {
   Category testCategory = new Category("Home");
   testCategory.save();
   assertTrue(testCategory.getId() > 0);
  }

  @Test
  public void find_returnsCategoryWithSameId_secondCategory() {
   Category firstCategory = new Category("Home");
   firstCategory.save();
   Category secondCategory = new Category("Work");
   secondCategory.save();
   assertEquals(Category.find(secondCategory.getId()), secondCategory);
  }

  @Test
  public void getTasks_retrievesAllTasksFromDatabase_taskslist() {
    Category testCategory = new Category("Chores");
    testCategory.save();
    Task firstTask = new Task("Mow the lawn", testCategory.getId());
    firstTask.save();
    Task secondTask = new Task("Do the dishes", testCategory.getId());
    secondTask.save();
    Task[] tasks = new Task[] { firstTask, secondTask };
    assertTrue(testCategory.getTasks().containsAll(Arrays.asList(tasks)));
  }

  // @Test
  // public void addTask_addsTaskToList_true() {
  //   Category testCategory = new Category("Home");
  //   Task testTask = new Task("Mow the lawn");
  //   testCategory.addTask(testTask);
  //   assertTrue(testCategory.getTasks().contains(testTask));
  // }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame() {
    Category firstCategory = new Category("Chores");
    Category secondCategory = new Category("Chores");
    assertTrue(firstCategory.equals(secondCategory));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Category myCategory = new Category("Chores");
    myCategory.save();
    assertTrue(Category.all().get(0).equals(myCategory));
  }

  @Test
  public void save_assignsIdToObject() {
    Category myCategory = new Category("Chores");
    myCategory.save();
    Category savedCategory = Category.all().get(0);
    assertEquals(myCategory.getId(), savedCategory.getId());
  }

  @Test
  public void delete_deletesCategory_true() {
    Category myCategory = new Category("Chores");
    myCategory.save();
    int myCategoryId = myCategory.getId();
    myCategory.delete(myCategoryId);
    assertEquals(null, Category.find(myCategoryId));
  }
}
