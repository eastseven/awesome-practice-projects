package cn.eastseven.webcrawler.model;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Set;

@Getter
@Setter
@ToString
public class BaseBook {

    protected long createTime = DateTime.now().getMillis();

    @Indexed
    protected String isbn;

    protected String image;

    protected String info;

    protected String contents;

    protected String postDate; //上架时间：2016-11-8

    protected String publishDate; //出版日期 2017 年1月

    protected String originName;

    protected String translator; // 译者

    protected String author;

    protected String press; //出版社

    protected String category1; // 所属分类1
    protected String category2; // 所属分类2
    protected String category3; // 所属分类3

    @Transient
    protected Set<BookCategory> categories = Sets.newHashSet();

    @DBRef
    protected BookCategory category;

    protected void doCategory(Elements items, BookOrigin origin) {
        for (int index = 0; index < items.size(); index++) {
            Element item = items.get(index);
            String value = item.text();
            String link = item.attr("href");

            BookCategory category = new BookCategory();
            category.setName(value);
            category.setUrl(link);
            category.setOrigin(origin);
            this.categories.add(category);

            switch (index) {
                case 0:
                    this.category1 = value;
                    break;
                case 1:
                    this.category2 = value;
                    break;
                case 2:
                    this.category3 = value;
                    break;
            }

            this.category = category;
        }
    }
}
