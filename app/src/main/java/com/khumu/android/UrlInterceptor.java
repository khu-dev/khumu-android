package com.khumu.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.khumu.android.articleDetail.ArticleDetailActivity;
import com.khumu.android.data.Article;
import com.khumu.android.data.Board;
import com.khumu.android.feed.HotBoardFeedActivity;
import com.khumu.android.feed.SingleBoardFeedActivity;
import com.khumu.android.home.HomeFragment;
import com.khumu.android.login.LoginActivity;
import com.khumu.android.util.Util;
import com.thefinestartist.finestwebview.FinestWebView;
import com.thefinestartist.finestwebview.FinestWebViewActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// Home WebView에서 사용할 UrlInterceptor
public class UrlInterceptor {
    private final static String TAG = "UrlInterceptor";
    private final List<String> RESOURCE_KIND_PLURALS = Arrays.asList("articles");

    private final Context context;
    // url을 열면 return true
    // 안 열 url이면 return false
    public boolean openUrl(Uri uri) {
        // 쿠뮤 관련 URL인 경우
        if (uri.getHost().equals(Uri.parse(Util.getKhumuWebRootUrl()).getHost())) {
            List<String> pathSegments = uri.getPathSegments();
            Log.d(TAG, "openUrl: " + "요청 path: " + uri.getPath());
            Log.d(TAG, "openUrl: " + "요청 pathSegments: " + pathSegments);
            if (pathSegments.size() == 1) {
                String resourceKindPlural = pathSegments.get(0);
                if (resourceKindPlural.equals("logout")) {
                    KhumuApplication.clearKhumuAuthenticationConfig();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    ((AppCompatActivity)context).finish();
                    return true;
                }
                if (RESOURCE_KIND_PLURALS.contains(resourceKindPlural)) {
                    switch (resourceKindPlural) {
                        // 게시판 하나가 아닌 게시판 리스트를 조회하는 경우
                        case "articles": {
                            String board = uri.getQueryParameter("board");
                            if (board == null) {
                                Log.w(TAG, "openUrl: board가 null입니다.");
                                return false;
                            }
                            if (board.equals("hot")) {
                                Intent intent = new Intent(context, HotBoardFeedActivity.class);
                                context.startActivity(intent);
                                return true;
                            } else if (Arrays.asList("liked", "bookmarked", "commented").contains(board)) {
                                String boardDisplayName = "게시판";
                                if (board.equals("liked")) {
                                    boardDisplayName = "좋아요한 게시글";
                                } else if (board.equals("bookmarked")) {
                                    boardDisplayName = "북마크한 게시글";
                                }  else if (board.equals("commented")) {
                                    boardDisplayName = "댓글단 게시글";
                                }
                                Intent intent = new Intent(context, SingleBoardFeedActivity.class);
                                intent.putExtra("board", Board.builder().name(board).displayName(boardDisplayName).build());
                                context.startActivity(intent);
                                return true;
                            }
                        } break;
                    }
                }
            } else if (2 <= pathSegments.size()) {
                String resourceKindPlural = pathSegments.get(0);
                Long resourceId = null;
                try{
                    resourceId = Long.valueOf(pathSegments.get(1));
                } catch (Exception e){
                    e.printStackTrace();
                }
                if (RESOURCE_KIND_PLURALS.contains(resourceKindPlural)) {
                    Log.d(TAG, "shouldOverrideUrlLoading: 요청 URL이 override하고자 하는 리소스를 나타냅니다. " + resourceKindPlural);
                    switch (resourceKindPlural) {
                        case "articles":{
                            Log.d(TAG, "shouldOverrideUrlLoading: resourceKindPlural: article이므로 article 관련 activity를 띄웁니다.");
                            if (resourceId == null) {
                                Toast.makeText(context, "올바른 게시글을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(context, ArticleDetailActivity.class);
                                Article tmpArticle = new Article();
                                // 엥 아직 얜 Integer쓰네. 나중에 바꿔야겠다...
                                tmpArticle.setId(resourceId.intValue());
                                intent.putExtra("article", tmpArticle);
                                intent.putExtra("toolbarTitle", "홈으로");
                                context.startActivity(intent);
                            }
                            return true;
                        }
                    }
                }
            }
        } else{
            // 외부 URL인 경우
            new FinestWebView.Builder(context)
                    .titleColor(ContextCompat.getColor(context, R.color.white))
                    .show(uri.toString());

            return true;
        }

        return false;
    }
}
