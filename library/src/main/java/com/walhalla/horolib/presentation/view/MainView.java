package com.walhalla.horolib.presentation.view;

import com.walhalla.horolib.beans.ZodiacSignAstro;

public interface MainView
{



    //ads
    void interstialBuild();

    void showBottomBanner(boolean b);

    void showForecast(ZodiacSignAstro zodiacSignAstro);
    void rewardedBuild();




    //==============================================================================================
//    @StateStrategyType(AddToEndStrategy.class)
//    void AddToEndStrategy();// — выполнить команду и добавить команду в конец очереди
//
//    @StateStrategyType(AddToEndSingleStrategy.class)
//    void AddToEndSingleStrategy();//@@@@@ — выполнить команду, добавить ее в конец очереди и удалить
//    // все ее предыдущие экземпляры
//
//    @StateStrategyType(SingleStateStrategy.class)
//    void SingleStateStrategy();// — выполнить команду, очистить очередь и добавить в нее команду
//
//    @StateStrategyType(SkipStrategy.class)
//    void SkipStrategy();// — выполнить команду
//
//    @StateStrategyType(OneExecutionStateStrategy.class)
//    void OneExecuteStrategy();// — выполнить команду при первой возможности
}
