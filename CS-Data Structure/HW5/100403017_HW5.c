#include<stdio.h>
#include<stdlib.h>
#include<string.h>

struct node//Huffman node
{
   struct node *Lnode;
   char data;
   float frequency;
   struct node *Rnode;
};
typedef struct node *nodePtr;

struct list//list node
{
   struct node *node;
   struct list *next;
};
typedef struct list *listPtr;

void inOrder(nodePtr treePtr);

int bit[20];//存編碼 
int bitcount=0;//算編碼長 
int ansbit[30][20];//存各個編碼 
int ansbitcount=0;//算各編碼長 
int count=0;
int ansbitsize[20];//算編碼數 
int counter[30];
int counterbit=0;
int temp1=0;

int main(void)
{
    char input[200];
    char *tokenPtr;
    int size=0;
    char data[100];
    float fre[100];
    int i,j;
    char tmpchar;
    float tmpfloat;
    listPtr listptr=NULL;
    float total=0.0;
    


	printf("input data(ex: a 0.2 b 0.3 c 0.5):\n");
	gets(input);//取得輸入存到input
	
    tokenPtr=strtok(input," ");//將input以空格切割 
    while(tokenPtr!=NULL)
    {
    	data[size]=*tokenPtr;
    	tokenPtr=strtok(NULL," ");//繼續切割 
    	fre[size++]=atof(tokenPtr);
    	tokenPtr=strtok(NULL," ");//繼續切割 
    }
    for(i=0;i<size;i++)//算總頻率 
    {
        total+=fre[i];        
    }
    if(total!=1)//如果頻率總和不等於一，錯誤 
    {
        printf("The sum of frequency is not equal to 1 ");
        return 1;
    }
    
    for(i=size-1;i>0;i--)//先對data排序 
    {
        for(j=0;j<i;j++)
        {
            if(fre[j]>=fre[j+1])
            {
                tmpchar=data[j];
                data[j]=data[j+1];
                data[j+1]=tmpchar;
                tmpfloat=fre[j];
                fre[j]=fre[j+1];
                fre[j+1]=tmpfloat;
            }
        }
    }

    for(i=size-1;i>=0;i--)
    {
        //把每份data放進Huffman的node裡 
        nodePtr tmpnodePtr=NULL;
        tmpnodePtr=malloc(sizeof(struct node));
        tmpnodePtr->data=data[i];
        tmpnodePtr->frequency=fre[i];
        tmpnodePtr->Lnode=NULL;
        tmpnodePtr->Rnode=NULL;
        //把每個Huffman node放進list node裡 
        listPtr tmplistPtr=NULL;
        tmplistPtr=malloc(sizeof(struct list));
        tmplistPtr->node=tmpnodePtr;
        tmplistPtr->next=listptr;
        listptr=tmplistPtr;
    }
    
    while((listptr->node)->frequency!=1)
    {
        listPtr mix=malloc(sizeof(struct list));//產生一個新list node 
        
        nodePtr fathernode=malloc(sizeof(struct node));//產生新父節點 
        fathernode->Lnode=listptr->node;//將list中第一個node(權值最小者)放到新父節點的左子樹 
        
        listPtr tmplistPtr=listptr;//移除list中最小頻率者 
        listptr=listptr->next;
        free(tmplistPtr);
        
        fathernode->Rnode=listptr->node;//再將list中第一個node(權值最小者)放到新父節點的右子樹 
        
        tmplistPtr=listptr;//移除list中最小頻率者 
        listptr=listptr->next;
        free(tmplistPtr);
        
        fathernode->frequency=(fathernode->Rnode)->frequency+(fathernode->Lnode)->frequency;//父節點權值=兩子節點權值相加 
        
        mix->node=fathernode;//將父節點放進新的list node中 
        
        listPtr insertPtr=listptr;
        
        while(insertPtr!=NULL)//將新的lsit node重新插入list中 
        {
            if((insertPtr->node)->frequency<=(mix->node)->frequency&&(insertPtr->next==NULL||((insertPtr->next)->node)->frequency>(mix->node)->frequency))
            {
                mix->next=insertPtr->next;
                insertPtr->next=mix;
                break;
            }
            if((insertPtr->node)->frequency>(mix->node)->frequency)
            {
                mix->next=listptr;
                listptr=mix;
                break;
            }
            insertPtr=insertPtr->next;
        }
        if(listptr==NULL)
        {
            mix->next=NULL;
            listptr=mix;
        }
        
    }

    inOrder(listptr->node);
    
    for(i=size-1;i>0;i--)//先對data排序 
    {
        for(j=0;j<i;j++)
        {
            if(counter[j]>=counter[j+1])
            {
                temp1=counter[j];
                counter[j]=counter[j+1];
                counter[j+1]=temp1;
            }
        }
    }
    for(i=0;i<size;i++)//印出結果 
    {
        printf("%c:",97+counter[i]);
        for(j=0;j<ansbitsize[counter[i]];j++)
        {
            printf("%d",ansbit[counter[i]][j]);
        }
        printf("\n");
    }
    
    return 0;
}

void inOrder(nodePtr treePtr)
{
    if(treePtr!=NULL)
    {
        bit[bitcount++]=0;//往左走為0 
        inOrder(treePtr->Lnode);
        bitcount--;//退回來時將bitcount減掉 
        bit[bitcount++]=1;//往右走為1 
        inOrder(treePtr->Rnode);
        bitcount--;//退回來時將bitcount減掉 
        if(treePtr->Lnode==NULL&&treePtr->Rnode==NULL)//檢驗是否為樹葉 
        {
            ansbitcount=treePtr->data-97;//以ASCII算出字母應放在ansbit中哪個位置 
            counter[counterbit++]=ansbitcount;
            for(count=0;count<=bitcount;count++)
            {
                ansbit[ansbitcount][count]=bit[count];//將bit中的編碼寫入ansbit中對應的位置 
            }
            ansbitsize[ansbitcount]=bitcount;//記錄此編碼長度 
        }
   }
}
