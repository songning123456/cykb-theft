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
import org.jsoup.select.Elements;
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

    @Test
    public void theftFeiku() {
        String baseUrl = "http://www.feiku.org/shuku/quanbu_default_0_0_0_0_0_0_";
        for (int i = 0, iLen = 70; i < 5; i++) {
            try {
                String sourceUrl = baseUrl + (i + 1) + ".html";
                Document pageNovelsDoc = HttpUtil.getHtmlFromUrl(sourceUrl, true);
                Elements liElements = pageNovelsDoc.getElementsByClass("books-list").get(0).getElementsByTag("li");
                for (int j = 0, jLen = liElements.size(); j < 5; j++) {
                    try {
                        Element aElement = liElements.get(j).getElementsByTag("a").get(0);
                        String novelsUrl = aElement.attr("href");
                        List<Novels> jNovels = novelsRepository.findBySourceUrl(novelsUrl);
                        if (jNovels != null && jNovels.size() > 0) {
                            continue;
                        }
                        Document novelsDoc = HttpUtil.getHtmlFromUrl(novelsUrl, true);
                        String coverUrl = novelsDoc.getElementsByClass("bookcover-l").get(0).getElementsByTag("img").get(0).attr("src");
                        Element bookInfo = novelsDoc.getElementsByClass("book-intro").get(0);
                        String paramTime = bookInfo.getElementsByTag("b").get(1).html();
                        int left = paramTime.indexOf("(");
                        int right = paramTime.indexOf(")");
                        String strUpdateTime = paramTime.substring(left + 1, right) + " 00:00:00";
                        Date updateTime = DateUtil.strToDate(strUpdateTime, "yyyy-MM-dd HH:mm:ss");
                        String bookHtml = bookInfo.html();
                        int firstBr = bookHtml.indexOf("<br>");
                        String introduction = bookHtml.substring(0, firstBr);
                        introduction = introduction.replaceAll("　　&nbsp;&nbsp;&nbsp;&nbsp;", "");
                        int firstB = bookHtml.indexOf("</b>");
                        int lastBr = bookHtml.lastIndexOf("<br>");
                        String[] info = bookHtml.substring(firstB + 5, lastBr - 2).split(" ");
                        String title = info[0];
                        String author = info[1];
                        String category = info[2];
                        String latestChapter = bookInfo.getElementsByTag("a").html();
                        Thread.sleep(1);
                        Long createTime = DateUtil.dateToLong(new Date());
                        Novels novels = Novels.builder().title(title).author(author).sourceUrl(novelsUrl).sourceName("飞库小说").category(category).createTime(createTime).coverUrl(coverUrl).introduction(introduction).latestChapter(latestChapter).updateTime(updateTime).build();
                        novels = novelsRepository.save(novels);
                        String listUrl = novelsDoc.getElementsByClass("catalogbtn").get(0).attr("href");
                        Document listDoc = HttpUtil.getHtmlFromUrl(listUrl, true);
                        Elements chapterList = listDoc.getElementsByClass("chapter-list").get(0).getElementsByTag("a");
                        for (int k = 0, kLen = chapterList.size(); k < 5; k++) {
                            try {
                                String contentUrl = "http://www.feiku.org" + chapterList.get(k).attr("href");
                                String chapter = chapterList.get(k).html();
                                List<Chapters> kChapters = chaptersRepository.findByChapterAndNovelsId(chapter, novels.getId());
                                if (kChapters != null && kChapters.size() > 0) {
                                    continue;
                                }
                                Document contentDoc = HttpUtil.getHtmlFromUrl(contentUrl, true);
                                String content = contentDoc.getElementsByClass("article-con").get(0).html();
                                Date chapterUpTime = DateUtil.intervalTime(strUpdateTime, kLen - k - 1);
                                Chapters chapters = Chapters.builder().chapter(chapter).content(content).novelsId(novels.getId()).updateTime(chapterUpTime).build();
                                chaptersRepository.save(chapters);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testBr() {
        String url = "http://www.feiku.org/book/2056.html";
        Document document = HttpUtil.getHtmlFromUrl(url, true);
        Element bookInfo = document.getElementsByClass("book-intro").get(0);
        String paramTime = bookInfo.getElementsByTag("b").get(1).html();
        int left = paramTime.indexOf("(");
        int right = paramTime.indexOf(")");
        String strUpdateTime = paramTime.substring(left + 1, right);
        String bookHtml = bookInfo.html();
        int firstBr = bookHtml.indexOf("<br>");
        String introduction = bookHtml.substring(0, firstBr);
        int firstB = bookHtml.indexOf("</b>");
        int lastBr = bookHtml.lastIndexOf("<br>");
        String[] info = bookHtml.substring(firstB + 5, lastBr - 2).split(" ");
        String title = info[0];
        String author = info[1];
        String category = info[2];
        String chapter = bookInfo.getElementsByTag("a").html();
        System.out.println("");
    }

}
