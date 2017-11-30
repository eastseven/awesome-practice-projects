package cn.eastseven.spark.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author eastseven
 */
@Slf4j
@Data
@Configuration
public class KeyWordsProperties {

    @Value("${app.industry.categories.file.path}")
    String path;

    private Map<String, String[]> keyWordsMap = Maps.newHashMap();

    @PostConstruct
    public void init() throws Exception {
        log.info("app.industry.categories.file.path = {}", path);

        Workbook workbook = WorkbookFactory.create(Paths.get(path).toFile());
        Sheet sheet = workbook.getSheetAt(0);
        Row firstRow = sheet.getRow(0);

        List<Cell> cellList = Lists.newArrayList(firstRow.cellIterator())
                .stream().filter(cell -> StringUtils.isNotBlank(cell.getStringCellValue())).collect(Collectors.toList());
        Map<Integer, String> categoryMap = cellList.stream().collect(Collectors.toMap(cell -> cell.getColumnIndex(), cell -> ""));

        List<Row> rowList = Lists.newArrayList(sheet.rowIterator()).stream().filter(row -> row.getRowNum() > 0).collect(Collectors.toList());
        rowList.forEach(row -> {
            for (Integer cellnum : categoryMap.keySet()) {
                String keyWords = row.getCell(cellnum).getStringCellValue();
                if (StringUtils.isNotBlank(keyWords)) {
                    categoryMap.put(cellnum, String.join(",", categoryMap.get(cellnum), keyWords));
                }
            }
        });

        keyWordsMap = cellList.stream()
                .collect(Collectors.toMap(cell -> cell.getStringCellValue(),
                        cell -> categoryMap.get(cell.getColumnIndex()).replaceFirst(",", "").split(",")));

        workbook.close();
    }
}
