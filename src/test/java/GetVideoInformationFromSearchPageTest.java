import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetVideoInformationFromSearchPageTest {

    private WebDriver driver;
    private String path = System.getProperty("user.dir");

    @BeforeTest
    public void setup(){

        System.out.println(path);
        System.setProperty("webdriver.chrome.driver", path + "\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.youtube.com/");
    }

    @Test
    public void saveVideoTitlesInJSONFile () throws IOException {

        WebElement searchBox = driver.findElement(By.cssSelector("div#search-input > input#search"));
        searchBox.click();
        searchBox.sendKeys("vespa PK");

        WebElement searchButton = driver.findElement(By.cssSelector("button#search-icon-legacy > .style-scope.ytd-searchbox"));
        searchButton.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement resultfromSearch = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(".//ytd-item-section-renderer/div[@id='contents']")));
        List<WebElement> resultListXpath = resultfromSearch.findElements(By.xpath("//div[@id='dismissable']//div[@id='meta']/div[@id='title-wrapper']//a[@id='video-title']"));

        List <String> outputListString = new ArrayList<>();
        //SAVE INFO FROM WEBELEMENT LIST IN A LIST STRING
        for ( WebElement result : resultListXpath){
            outputListString.add(result.getText());
        }

        JSONObject titleListJSONObject = new JSONObject();
        //JSONArray outputJSONArray = new JSONArray();
        JSONObject outputJSONObject = new JSONObject();
        //SAVE INFO FROM ARRAY LIST INTO A JSON OBJECT
        for ( String inputString : outputListString )
        {
            titleListJSONObject.put("title["+ outputListString.indexOf(inputString) +"]",inputString);
            System.out.println("titleJSONObject" + titleListJSONObject);
        }
        //outputJSONArray.put(titleListJSONObject);
        //System.out.println("outputJSONArray" + outputJSONArray);
        outputJSONObject.put("titleList",titleListJSONObject);
        System.out.println("outputJSONObject" + outputJSONObject);

        try {
            FileWriter JSONFile = new FileWriter(path + "\\outputJSONObject.json");
            JSONFile.write(outputJSONObject.toString());
            JSONFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterTest
    public void exit() {
        driver.quit();
    }

}
