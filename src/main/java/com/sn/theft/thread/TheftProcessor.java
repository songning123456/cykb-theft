package com.sn.theft.thread;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: songning
 * @date: 2020/3/29 13:19
 */
@Component
@Slf4j
public class TheftProcessor {

    @Autowired
    private NovelsRepository novelsRepository;

    @Autowired
    private ChaptersRepository chaptersRepository;

    @Async("TheftExecutor")
    public void testTheft() {
        try {
            Thread.sleep(30 * 1000);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("测试 theft");
    }

    @Async("TheftExecutor")
    public void theftBiquge() {
        try {
            Document html;
            String source = "http://www.xbiquge.la/xiaoshuodaquan/";
            html = HttpUtil.getHtmlFromUrl(source, true);
            Element mainElement = html.getElementById("main");
            for (int i = 0, iLen = mainElement.getElementsByClass("novellist").size(); i < iLen; i++) {
                try {
                    Element ulElement = mainElement.getElementsByClass("novellist").get(i).getElementsByTag("ul").get(0);
                    for (int j = 0, jLen = ulElement.getElementsByTag("a").size(); j < jLen; j++) {
                        try {
                            Novels novels;
                            String bookUrl = ulElement.getElementsByTag("a").get(j).attr("href");
                            // 判断 是否已经存在，如果存在则跳过
                            List<Novels> jNovels = novelsRepository.findBySourceUrl(bookUrl);
                            if (jNovels != null && jNovels.size() > 0) {
                                continue;
                            }
                            Document childDoc;
                            try {
                                childDoc = HttpUtil.getHtmlFromUrl(bookUrl, true);
                            } catch (Exception e) {
                                e.printStackTrace();
                                continue;
                            }
                            Element maininfoElement = childDoc.getElementById("maininfo");
                            String coverUrl = childDoc.getElementById("sidebar").getElementsByTag("img").get(0).attr("src");
                            Element infoElement = maininfoElement.getElementById("info");
                            String introduction = maininfoElement.getElementById("intro").getElementsByTag("p").get(1).html();
                            String author = infoElement.getElementsByTag("p").get(0).html().split("：")[1];
                            String latestChapter = infoElement.getElementsByTag("p").get(3).getElementsByTag("a").get(0).html();
                            Thread.sleep(1);
                            Long createTime = DateUtil.dateToLong(new Date());
                            String title = infoElement.getElementsByTag("h1").html();
                            String category = childDoc.getElementsByClass("con_top").get(0).getElementsByTag("a").get(2).html();
                            String strUpdateTime = infoElement.getElementsByTag("p").get(2).html().split("：")[1];
                            Date updateTime = DateUtil.strToDate(strUpdateTime, "yyyy-MM-dd HH:mm:ss");
                            novels = Novels.builder().title(title).author(author).sourceUrl(bookUrl).sourceName("笔趣阁").category(category).createTime(createTime).coverUrl(coverUrl).introduction(introduction).latestChapter(latestChapter).updateTime(updateTime).build();
                            novels = novelsRepository.save(novels);
                            String novelsId = novels.getId();
                            Chapters chapters;
                            Element dlElement = childDoc.getElementById("list").getElementsByTag("dl").get(0);
                            for (int k = 0, kLen = dlElement.getElementsByTag("dd").size(); k < kLen; k++) {
                                try {
                                    Element a = dlElement.getElementsByTag("dd").get(k).getElementsByTag("a").get(0);
                                    String chapter = a.html();
                                    List<Chapters> kChapters = chaptersRepository.findByChapterAndNovelsId(chapter, novelsId);
                                    if (kChapters != null && kChapters.size() > 0) {
                                        continue;
                                    }
                                    String chapterUrl = "http://www.xbiquge.la/" + a.attr("href");
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
                                    log.error(e.getMessage());
                                }
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async("TheftExecutor")
    public void theft147() {
        String prefixUrl = "http://www.147xiaoshuo.com/sort/";
        List<Integer> suffixList = Arrays.asList(1, 2, 3, 4, 6, 7, 10, 11, 8, 12, 9, 5);
        for (int i = 0, iLen = suffixList.size(); i < iLen; i++) {
            try {
                String fullUrl = prefixUrl + suffixList.get(i) + "/";
                Document novelsDoc = HttpUtil.getHtmlFromUrl(fullUrl, true);
                Elements liElements = novelsDoc.getElementById("main").getElementsByClass("novellist").get(0).getElementsByTag("ul").get(0).getElementsByTag("li");
                for (int j = 0, jLen = liElements.size(); j < jLen; j++) {
                    try {
                        String AContent = liElements.get(j).getElementsByTag("a").get(0).attr("href");
                        String contentUrl = "http://www.147xiaoshuo.com/" + AContent;
                        List<Novels> jNovels = novelsRepository.findBySourceUrl(contentUrl);
                        if (jNovels != null && jNovels.size() > 0) {
                            continue;
                        }
                        Novels novels;
                        Document contentDoc = HttpUtil.getHtmlFromUrl(contentUrl, true);
                        String coverUrl = contentDoc.getElementById("fmimg").getElementsByTag("img").get(0).attr("src");
                        String introduction = contentDoc.getElementById("intro").html();
                        String author = contentDoc.getElementById("info").getElementsByTag("p").get(0).html().split("：")[1];
                        String latestChapter = contentDoc.getElementById("info").getElementsByTag("p").get(3).getElementsByTag("a").get(0).html();
                        Thread.sleep(1);
                        Long createTime = DateUtil.dateToLong(new Date());
                        String title = contentDoc.getElementById("info").getElementsByTag("h1").get(0).html();
                        String category = contentDoc.getElementsByClass("con_top").get(0).getElementsByTag("a").get(1).html();
                        String strUpdateTime = contentDoc.getElementById("info").getElementsByTag("p").get(2).html().split("：")[1];
                        Date updateTime = DateUtil.strToDate(strUpdateTime, "yyyy-MM-dd HH:mm:ss");
                        novels = Novels.builder().title(title).author(author).sourceUrl(contentUrl).sourceName("147小说").category(category).createTime(createTime).coverUrl(coverUrl).introduction(introduction).latestChapter(latestChapter).updateTime(updateTime).build();
                        novels = novelsRepository.save(novels);
                        String novelsId = novels.getId();
                        Chapters chapters;
                        Elements ddElements = contentDoc.getElementById("list").getElementsByTag("dd");
                        for (int k = 0, kLen = ddElements.size(); k < kLen; k++) {
                            try {
                                Element chapterElement = ddElements.get(k).getElementsByTag("a").get(0);
                                String chapter = chapterElement.html();
                                List<Chapters> kChapters = chaptersRepository.findByChapterAndNovelsId(chapter, novelsId);
                                if (kChapters != null && kChapters.size() > 0) {
                                    continue;
                                }
                                String chapterUrl = "http://www.147xiaoshuo.com/" + chapterElement.attr("href");
                                Document chapterDoc = HttpUtil.getHtmlFromUrl(chapterUrl, true);
                                String content = chapterDoc.getElementById("content").html();
                                Date chapterUpTime = DateUtil.intervalTime(strUpdateTime, kLen - k - 1);
                                chapters = Chapters.builder().chapter(chapter).content(content).novelsId(novelsId).updateTime(chapterUpTime).build();
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

    @Async("TheftExecutor")
    public void theftTtsb() {
        String prefixUrl = "http://www.ttshuba.net/fenlei/";
        for (int i = 1; i <= 10; i++) {
            try {
                String fullUrl = prefixUrl + i + "/1.html";
                Document novelsDoc = HttpUtil.getHtmlFromUrl(fullUrl, true);
                this.handleTiantian(novelsDoc);
                // 分页处理后续页面
                int lastPage = Integer.parseInt(novelsDoc.getElementById("pagelink").getElementsByClass("last").get(0).html());
                if (lastPage == 1) {
                    continue;
                }
                for (int m = 2; m <= lastPage; m++) {
                    String pageUrl = prefixUrl + i + "/" + m + ".html";
                    Document pageNovelsDoc = HttpUtil.getHtmlFromUrl(pageUrl, true);
                    this.handleTiantian(pageNovelsDoc);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleTiantian(Document document) {
        Elements picElements = document.getElementById("alist").getElementsByClass("pic");
        for (int j = 0, jLen = picElements.size(); j < jLen; j++) {
            try {
                String dictionaryUrl = picElements.get(j).getElementsByTag("a").get(0).attr("href");
                List<Novels> jNovels = novelsRepository.findBySourceUrl(dictionaryUrl);
                if (jNovels != null && jNovels.size() > 0) {
                    continue;
                }
                Novels novels;
                Document dictionaryDoc = HttpUtil.getHtmlFromUrl(dictionaryUrl, true);
                String coverUrl = dictionaryDoc.getElementById("fmimg").getElementsByTag("img").get(0).attr("src");
                String introduction = document.getElementById("alist").getElementsByClass("info").get(j).getElementsByClass("intro").get(0).html();
                String author = dictionaryDoc.getElementById("info").getElementsByTag("p").get(0).html().split("：")[1];
                String latestChapter = dictionaryDoc.getElementById("info").getElementsByTag("p").get(5).getElementsByTag("a").html();
                Thread.sleep(1);
                Long createTime = DateUtil.dateToLong(new Date());
                String title = dictionaryDoc.getElementById("info").getElementsByTag("h1").get(0).html();
                String category = dictionaryDoc.getElementById("info").getElementsByTag("p").get(1).html().split("：")[1];
                String strUpdateTime = DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
                Date updateTime = DateUtil.strToDate(strUpdateTime, "yyyy-MM-dd HH:mm:ss");
                novels = Novels.builder().title(title).author(author).sourceUrl(dictionaryUrl).sourceName("天天书吧").category(category).createTime(createTime).coverUrl(coverUrl).introduction(introduction).latestChapter(latestChapter).updateTime(updateTime).build();
                novels = novelsRepository.save(novels);
                String novelsId = novels.getId();
                Chapters chapters;
                Elements ddElements = dictionaryDoc.getElementById("list").getElementsByTag("dd");
                for (int k = 0, kLen = ddElements.size(); k < kLen; k++) {
                    try {
                        Element chapterElement = ddElements.get(k).getElementsByTag("a").get(0);
                        String chapter = chapterElement.html();
                        List<Chapters> kChapters = chaptersRepository.findByChapterAndNovelsId(chapter, novelsId);
                        if (kChapters != null && kChapters.size() > 0) {
                            continue;
                        }
                        String chapterUrl = dictionaryUrl + chapterElement.attr("href");
                        Document chapterDoc = HttpUtil.getHtmlFromUrl(chapterUrl, true);
                        String content = chapterDoc.getElementById("TXT").html();
                        int headIndex = content.indexOf("&nbsp;&nbsp;&nbsp;&nbsp;");
                        int tailIndex = content.indexOf("<div class=\"bottem\">");
                        content = content.substring(headIndex, tailIndex);
                        Date chapterUpTime = DateUtil.intervalTime(strUpdateTime, kLen - k - 1);
                        chapters = Chapters.builder().chapter(chapter).content(content).novelsId(novelsId).updateTime(chapterUpTime).build();
                        chaptersRepository.save(chapters);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
