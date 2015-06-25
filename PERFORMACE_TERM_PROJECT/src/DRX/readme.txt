關於Active Traffic sleep本部分的模擬結果跟paper上差不多，模擬時間的長短會高度影響跑出來的結果，以上是用period=10Kms, seed*1跑出來的結果，隨著period拉長，此model無法完全模擬真實狀況而造成高估，也就是跑出來的sleep time比真實狀況高，ex. period=1KKms, TLC=80時就94%。
關於Active Traffic delay本部分的模擬結果跟paper上差不多，模擬時間的長短會高度影響跑出來的結果，以上是用period=10Kms, seed*1跑出來的結果，隨著period拉長，此model無法完全模擬真實狀況而造成高估，也就是跑出來的delay time比真實狀況高，ex. period=1KKms, TLC=2560時就1.1s。
關於Background Traffic delay本部分的模擬結果跟paper上差挺多的，後來才想到同時間不可能只有一個 Background Traffic seed，將seed數量增加十倍後數值也大幅增加，本部分為作者沒有說明清楚seed數量，亦或是使用OPNET的default值。

本篇作者使用OPNET Modeler做模擬，而因為我本身非CN研究專長，要熟悉軟體不如直接下去寫模擬，所以也遇到不少可能本身作者代default值卻沒有在paper中寫清楚的部分:
(1)	模擬時間的長短: 一開始設定1KKms太長了，跑出來的sleep time百分比會過高，值到調整成10Kms才正常，歸咎原因在於長時間區間下，session跟session之間的關係很難用active traffic簡單帶過，例如串流delay對於使用者也是高度敏感，但他的封包形式確是高度周期性而非exponential。
(2)	Active Traffic Seeds: 短區間中seed數量不大影響實驗的結果，長時間區間seed數量/時間位置高度影響結果。
(3)	Background Traffic: 同時間background apps絕對不只一個，所以一定要多設定幾個。
