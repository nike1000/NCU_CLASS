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

int bit[20];//�s�s�X 
int bitcount=0;//��s�X�� 
int ansbit[30][20];//�s�U�ӽs�X 
int ansbitcount=0;//��U�s�X�� 
int count=0;
int ansbitsize[20];//��s�X�� 
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
	gets(input);//���o��J�s��input
	
    tokenPtr=strtok(input," ");//�Ninput�H�Ů���� 
    while(tokenPtr!=NULL)
    {
    	data[size]=*tokenPtr;
    	tokenPtr=strtok(NULL," ");//�~����� 
    	fre[size++]=atof(tokenPtr);
    	tokenPtr=strtok(NULL," ");//�~����� 
    }
    for(i=0;i<size;i++)//���`�W�v 
    {
        total+=fre[i];        
    }
    if(total!=1)//�p�G�W�v�`�M������@�A���~ 
    {
        printf("The sum of frequency is not equal to 1 ");
        return 1;
    }
    
    for(i=size-1;i>0;i--)//����data�Ƨ� 
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
        //��C��data��iHuffman��node�� 
        nodePtr tmpnodePtr=NULL;
        tmpnodePtr=malloc(sizeof(struct node));
        tmpnodePtr->data=data[i];
        tmpnodePtr->frequency=fre[i];
        tmpnodePtr->Lnode=NULL;
        tmpnodePtr->Rnode=NULL;
        //��C��Huffman node��ilist node�� 
        listPtr tmplistPtr=NULL;
        tmplistPtr=malloc(sizeof(struct list));
        tmplistPtr->node=tmpnodePtr;
        tmplistPtr->next=listptr;
        listptr=tmplistPtr;
    }
    
    while((listptr->node)->frequency!=1)
    {
        listPtr mix=malloc(sizeof(struct list));//���ͤ@�ӷslist node 
        
        nodePtr fathernode=malloc(sizeof(struct node));//���ͷs���`�I 
        fathernode->Lnode=listptr->node;//�Nlist���Ĥ@��node(�v�ȳ̤p��)���s���`�I�����l�� 
        
        listPtr tmplistPtr=listptr;//����list���̤p�W�v�� 
        listptr=listptr->next;
        free(tmplistPtr);
        
        fathernode->Rnode=listptr->node;//�A�Nlist���Ĥ@��node(�v�ȳ̤p��)���s���`�I���k�l�� 
        
        tmplistPtr=listptr;//����list���̤p�W�v�� 
        listptr=listptr->next;
        free(tmplistPtr);
        
        fathernode->frequency=(fathernode->Rnode)->frequency+(fathernode->Lnode)->frequency;//���`�I�v��=��l�`�I�v�Ȭۥ[ 
        
        mix->node=fathernode;//�N���`�I��i�s��list node�� 
        
        listPtr insertPtr=listptr;
        
        while(insertPtr!=NULL)//�N�s��lsit node���s���Jlist�� 
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
    
    for(i=size-1;i>0;i--)//����data�Ƨ� 
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
    for(i=0;i<size;i++)//�L�X���G 
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
        bit[bitcount++]=0;//��������0 
        inOrder(treePtr->Lnode);
        bitcount--;//�h�^�ӮɱNbitcount� 
        bit[bitcount++]=1;//���k����1 
        inOrder(treePtr->Rnode);
        bitcount--;//�h�^�ӮɱNbitcount� 
        if(treePtr->Lnode==NULL&&treePtr->Rnode==NULL)//����O�_���� 
        {
            ansbitcount=treePtr->data-97;//�HASCII��X�r������bansbit�����Ӧ�m 
            counter[counterbit++]=ansbitcount;
            for(count=0;count<=bitcount;count++)
            {
                ansbit[ansbitcount][count]=bit[count];//�Nbit�����s�X�g�Jansbit����������m 
            }
            ansbitsize[ansbitcount]=bitcount;//�O�����s�X���� 
        }
   }
}
