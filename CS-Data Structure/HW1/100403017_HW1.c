//100403017 ¶À°ê¬O
//Stack by LinkedList

#include<stdio.h>
#include<stdlib.h>

struct stackNode
{
   int data;
   struct stackNode *nextPtr;
};

typedef struct stackNode StackNode;
typedef StackNode *StackNodePtr;

void push(StackNodePtr *topPtr,int info);
int pop(StackNodePtr *topPtr);
int isEmpty(void);
int isFull(void);
void printStack(StackNodePtr currentPtr);
void instructions();
int count=0;

int main(void)
{
   StackNodePtr stackPtr=NULL;
   int choice,value;
   
   do
   {
      instructions();
      scanf("%d",&choice);
      switch(choice)
      {
         case 1:
              if(isFull()==1)
              {
                 printf("Input a number.\n");
                 scanf("%d",&value);
                 push(&stackPtr,value); 
              }
              else
              {
                 printf("Error:Stack is full.\n"); 
              }
              printStack(stackPtr);
              break;
         case 2:
              if(isEmpty()==1)
              {
                 printf("Pop out:%d\n",pop(&stackPtr));
              }
              else
              {
                 printf("Error:Stack is empty.\n"); 
              }
              printStack(stackPtr);
              break;
         case 3:
              if(isFull()==0)
              {
                 printf("Stack is full.\n"); 
              }
              else
              {
                 printf("Stack is not full.\n"); 
              }
              printStack(stackPtr);
              break;
         case 4:
              if(isEmpty()==0)
              {
                 printf("Stack is empty.\n"); 
              }
              else
              {
                 printf("Stack is not empty.\n"); 
              }
              printStack(stackPtr);
              break;              
      }
   }while(choice!=0);
   return 0;
}

void instructions(void)
{
   printf("Select a function.\n1:Push\n2.Pop\n3.isFull\n4.isEmpty\n0.Exit\n");
}

void push(StackNodePtr *topPtr,int info)
{
   StackNodePtr newPtr;
   newPtr=malloc(sizeof(StackNode));

   newPtr->data=info;
   newPtr->nextPtr=*topPtr;
   *topPtr=newPtr;
   count++;
}

int pop(StackNodePtr *topPtr)
{
   StackNodePtr tempPtr;
   int popValue;
   
   tempPtr=*topPtr;
   popValue=(*topPtr)->data;
   *topPtr=(*topPtr)->nextPtr;
   count--;
   free(tempPtr);
   return popValue;
}

void printStack(StackNodePtr currentPtr)
{
   if(currentPtr==NULL)
   {
      printf("Stack:Empty\n\n\n\n\n");
   }
   else
   {
      printf("Stack:");
      while(currentPtr!=NULL)
      {
         printf("%d   ",currentPtr->data);
         currentPtr=currentPtr->nextPtr;
      }
      printf("\n\n\n\n\n");
   }
}

int isFull(void)
{
   return count==5?0:1;
}
int isEmpty(void)
{
   return count==0?0:1;
}
