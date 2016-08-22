#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct node
{
   struct node *Lnode;
   int data;
   struct node *Rnode;
};
typedef struct node *treeNodeptr;

void insertNode(treeNodeptr *treePtr,int number);
void inOrder(treeNodeptr treePtr);
void preOrder(treeNodeptr treePtr);
void postOrder(treeNodeptr treePtr);

int main(void)
{
   treeNodeptr TreeRootPtr=NULL;
   char input[100];//�x�s��J���r���}�C 
   int numArray[100];//�x�s�ഫ�᪺��� 
   int array=0;//�p�� 
   char *tokenPtr;//���ΨϥΤ��r������ 
   int i=0;//�p�� 
   //char temp="end";
      
   printf("Please input a number of sequence, split by space:");
   
   gets(input);//���o��J�s��input 
   
   tokenPtr=strtok(input," ");//�Ninput�H�Ů���� 
   while(tokenPtr!=NULL)
   {
      numArray[array++]=atoi(tokenPtr);//�নint�s�JnumArray 
      tokenPtr=strtok(NULL," ");//�~����� 
   }
   while(i<array)
   {
      insertNode(&TreeRootPtr,numArray[i++]);//�NnumArray����insertNode 
   }

   printf("inorder:");
   inOrder(TreeRootPtr);
   printf("\npreorder:");
   preOrder(TreeRootPtr);
   printf("\npostorder");
   postOrder(TreeRootPtr);
   printf("\nNode count:%d\n",array);

   return 0;
}

void insertNode(treeNodeptr *treePtr,int number)
{
   if(*treePtr==NULL)
   {
      *treePtr=malloc(sizeof(struct node));
      (*treePtr)->data=number;
      (*treePtr)->Lnode=NULL;
      (*treePtr)->Rnode=NULL;
   }
   else if(number>(*treePtr)->data)
   {
      insertNode(&((*treePtr)->Rnode),number);
   }
   else if(number<(*treePtr)->data)
   {
      insertNode(&((*treePtr)->Lnode),number);
   }
}

void inOrder(treeNodeptr treePtr)
{
   if(treePtr!=NULL)
   {
      inOrder(treePtr->Lnode);
      printf("%-3d",treePtr->data);
      inOrder(treePtr->Rnode);
   }
}

void preOrder(treeNodeptr treePtr)
{
   if(treePtr!=NULL)
   {
       printf("%-3d",treePtr->data);
       preOrder(treePtr->Lnode);
       preOrder(treePtr->Rnode);
   }
}

void postOrder(treeNodeptr treePtr)
{
   if(treePtr!=NULL)
   {
      postOrder(treePtr->Lnode);
      postOrder(treePtr->Rnode);
      printf("%-3d",treePtr->data);
   }
}
