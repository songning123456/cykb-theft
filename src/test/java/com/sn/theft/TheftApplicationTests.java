package com.sn.theft;

import com.sn.theft.entity.Chapters;
import com.sn.theft.entity.Novels;
import com.sn.theft.repository.ChaptersRepository;
import com.sn.theft.repository.NovelsRepository;
import com.sn.theft.util.DateUtil;
import com.sn.theft.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class TheftApplicationTests {

    @Autowired
    private NovelsRepository novelsRepository;
    @Autowired
    private ChaptersRepository chaptersRepository;

    @Test
    public void theftQushuba() {
        try {
            Document allNovelsDoc;
            String source = "http://www.qushuba.com/xiaoshuodaquan/";
            allNovelsDoc = HttpUtil.getHtmlFromUrl(source, true);
            Element mainElement = allNovelsDoc.getElementById("main");
            for (int i = 0, iLen = mainElement.getElementsByClass("novellist").size(); i < 1; i++) {
                try {
                    Element ulElement = mainElement.getElementsByClass("novellist").get(i).getElementsByTag("ul").get(0);
                    for (int j = 0, jLen = ulElement.getElementsByTag("a").size(); j < 1; j++) {
                        Novels novels;
                        String novelsUrl = ulElement.getElementsByTag("a").get(j).attr("href");
                        // 判断 是否已经存在，如果存在则跳过
                        List<Novels> jNovels = novelsRepository.findBySourceUrl(novelsUrl);
                        if (jNovels != null && jNovels.size() > 0) {
                            continue;
                        }
                        Document novelsDoc;
                        novelsDoc = HttpUtil.getHtmlFromUrl(novelsUrl, true);
                        Element maininfoElement = novelsDoc.getElementById("maininfo");
                        String coverUrl = novelsDoc.getElementById("sidebar").getElementsByTag("img").get(0).attr("src");
                        Element infoElement = maininfoElement.getElementById("info");
                        String introduction = maininfoElement.getElementById("intro").getElementsByTag("p").get(0).html();
                        String author = infoElement.getElementsByTag("p").get(0).html().split("：")[1];
                        String latestChapter = infoElement.getElementsByTag("p").get(3).getElementsByTag("a").get(0).html();
                        Thread.sleep(1);
                        Long createTime = DateUtil.dateToLong(new Date());
                        String title = infoElement.getElementsByTag("h1").html();
                        String category = novelsDoc.getElementsByClass("con_top").get(0).getElementsByTag("a").get(2).html();
                        String[] times = (infoElement.getElementsByTag("p").get(2).html().split("：")[1]).split(" ");
                        String strUpdateTime = times[0] + " " + times[1] + ":00";
                        Date updateTime = DateUtil.strToDate(strUpdateTime, "yyyy-MM-dd HH:mm:ss");
                        novels = Novels.builder().title(title).author(author).sourceUrl(novelsUrl).sourceName("趣书吧").category(category).createTime(createTime).coverUrl(coverUrl).introduction(introduction).latestChapter(latestChapter).updateTime(updateTime).build();
                        novels = novelsRepository.save(novels);
                        String novelsId = novels.getId();
                        Chapters chapters;
                        Element dlElement = novelsDoc.getElementById("list").getElementsByTag("dl").get(0);
                        for (int k = 0, kLen = dlElement.getElementsByTag("dd").size(); k < 1; k++) {
                            try {
                                Element a = dlElement.getElementsByTag("dd").get(k).getElementsByTag("a").get(0);
                                String chapter = a.html();
                                List<Chapters> kChapters = chaptersRepository.findByChapterAndNovelsId(chapter, novelsId);
                                if (kChapters != null && kChapters.size() > 0) {
                                    continue;
                                }
                                String chapterUrl = novelsUrl + a.attr("href");
                                Document contentDoc = HttpUtil.getHtmlFromUrl(chapterUrl, true);
                                String content;
                                try {
                                    content = contentDoc.getElementById("content").html();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    content = "加载出错了!";
                                }
                                Date chapterUpTime = DateUtil.intervalTime(strUpdateTime, kLen - k - 1);
                                chapters = Chapters.builder().chapter(chapter).content(content).novelsId(novelsId).updateTime(chapterUpTime).build();
                                chaptersRepository.save(chapters);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            log.error("最外层Exception: {}", e.getMessage());
        }
    }

}
