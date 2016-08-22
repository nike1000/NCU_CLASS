# 103-2 Computer Organization Pipeline Simulation Homework

## 作業要求

請同學以 C/C++/Java 程式語言實作課本 Pipeline 處理器之指令運作模擬程式。同學需實作 下列指令:「 lw」、「 sw」、「 add」、「 sub」、「 and」、「 or」、「 beq」、「 slt」。並且,能夠 偵測處理「data hazard」、「hazard with load」以及「branch hazard」三種 hazards。

## 程式的輸入與輸出

**輸入:** 

請依照以下公布的 32-bit 機器碼指令,其中包含需要處理 data hazard、hazard with load 以及 branch hazard。每題的指令順序都會放在「*.txt」檔案中,並由同學們撰寫的 模擬程式讀入。一共四題,另有一加分題,每一題都是獨立的,不會相互影響。

  1. 請同學的模擬程式從 "General.txt" 檔中,讀出四行指令,分別為:
    
    lw $3, 0x02($7)
    
    sub $2, $6, $7
    
    and $8, $5, $4
    
    slt $1, $5, $6
    
    將執行結果寫入 " genResult.txt" 檔案中。

  2. 請同學的模擬程式從 "Datahazard.txt" 檔中,讀出四行指令,分別為:
    
    sub $2, $4, $3
    
    and $4, $2, $3
    
    or $4, $4, $2 
    
    add $3, $2, $4
    
    將執行結果寫入 "dataResult.txt" 中。

  3. 請同學的模擬程式從 "Lwhazard.txt" 檔中,讀出四行指令,分別為:

    lw $2, 0x04($8)

    and $4, $2, $5

    or $4, $4, $2

    add $3, $2, $4

    將執行結果寫入 "loadResult.txt" 中。

  4. 請同學的模擬程式從 "Branchazard.txt" 檔中,讀出四行指令,分別為:

    beq $1, $5, 0x02 #(branch 至 lw 指令)

    and $3, $4, $5

    or $6, $7, $8

    lw $4, 0x00($0)

    將執行結果寫入 "branchResult.txt" 中。

  5. 請同學的模擬程式從 " Branchbonus.txt" 檔中,讀出五行指令,分別為:

    sub $2, $5, $0

    beq $1, $2, 0x02 #(branch 到 lw 指令)

    and $3, $4, $5

    or $6, $7, $8

    lw $4, 0x04($8)

    將執行結果寫入 "bonusResult.txt" 中。

P.S. Branch 指令第三個欄位應為 label,這裡為了方便而表示成數字形式,非正確寫法。

**輸出:**

請依照上述每一題規定,將結果分別寫入到「branchResult.txt」、「dataResult.txt」、 「genResult.txt」、「loadResult.txt」和「bonusResult.txt」(加分)之中。如有 hazard 需在 螢幕輸出提示訊息。輸出文件格式請按照以下範例(Output.txt),也就是列印出在每個 clock cycle 時,各個 pipeline registers 所儲存的值。「Instruction」和「Control signals」的 結果已 0/1 表示,其餘皆以十進位表示。「Control signals」的排列順序請參照老師投影 片「Chap4_2, pp.29」所示。

Example input: InstrIn.txt

Example output: Output.txt

## 其他

更多細節請參考： COHW103-pipeline.pdf
