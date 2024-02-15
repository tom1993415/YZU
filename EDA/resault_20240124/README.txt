報告內容參考report.docx report.pdf(提供兩版本)

pesudo code 參考 pesudo code.txt



程式主體 請參考s1126111_SA    (java code有要求檔名&&class名必須一致,且開頭必須英文,所以補上一個s)

各FUNC說明  (FUNC有註解掉,需要使用的話要取消註解)

main 主執行緒 主要是做讀檔產data
simulatedAnnealing 退火(SA)
generateLPModel 生成LP Model數學式
hMetisCostCalc 計算hMetis產生結果的cost


edge structure
Solution structure 包含 calculateCost 計算Cost


採編譯式,所以只有一隻SA檔


hmetis資料夾為hmetis產生結果

topic資料夾為老師提供資料與題目

LPmodel資料夾為generateLPModel FUNC 產生的LP MODEL

-------------------------------------------------------------------------------------------------------------------------------
2024/01/24 本次更新

Report 

Part II 公式調整 已有絕對值|Xi-Xj| 不需要1-
Part III 補上 LPmodel運行圖 hmetis運行圖 
Part IV 更新 hMetis的cost部分,原填寫 Scaled Cost 更換為新 Cost(程式計算)

程式更新

新增hMetisCostCalc FUNC 去計算 hmetis結果的Cost

新增README

更改部分檔名,符合題目需求




