// 일반적으론 Repository의 상위 계층으로서 동작하지만,
// 굳이 그렇게까지 할 필요는 없는 것 같고, 간혹 use case를 적용해야할 때에만 이용하는 것이
// 간편할 듯 하다.
package com.khumu.android.usecase;

import com.khumu.android.KhumuApplication;
import com.khumu.android.data.Article;
import com.khumu.android.repository.ArticleRepository;

import javax.inject.Inject;

import dagger.Module;

@Module
public class ArticleUseCase {
    @Inject
    ArticleRepository articleRepository;

    @Inject
    public ArticleUseCase(){
        KhumuApplication.container.inject(this);
    }

    //내가 작성자인가
    public boolean amIAuthor(Article a){
        return a.getAuthor().getUsername().equals(KhumuApplication.getUsername());
    }

    public String getAuthorNickname(Article a){
        String kind = a.getKind();
        if(kind.equals("anonymous")){
            return "익명";
        } else if (kind.equals("named")){
            return a.getAuthor().getNickname();
        }
        else{
            return "알 수 없는 종류의 작성자";
        }
    }

}
