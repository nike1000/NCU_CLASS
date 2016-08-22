#include<stdio.h>
#include<stdlib.h>

struct stackNode
{
   int mapx;
   int mapy;
   struct stackNode *nextPtr;
};

typedef struct stackNode StackNode;
typedef StackNode *StackNodePtr;

void push(StackNodePtr *topPtr,int x,int y);
void pop(StackNodePtr *topPtr);
int isEmpty(void);
void printmap(StackNodePtr currentPtr);
void load(void);
int checkStack(StackNodePtr currentPtr,int checkx,int checky);

int map[7][7];
int row=0,column=0;
int point;
int x=0;
int y=0;
StackNodePtr stackPtr=NULL;

int main(void)
{
   load();
   push(&stackPtr,x,y);
   
   while(x<7||y<7)
   {
      if(map[x][y+1]==0&&checkStack(stackPtr,x+1,y)!=0&&y!=6)
      {
         push(&stackPtr,x,++y);
         printf("%d\t%d\n",x,y);
      }
      else if(map[x+1][y]==0&&checkStack(stackPtr,x+1,y)!=0&&x!=6)
      {
         push(&stackPtr,++x,y);
         printf("%d\t%d\n",x,y);
      }
      else if(map[x][y-1]==0&&checkStack(stackPtr,x,y-1)!=0&&y>0)
      {
         push(&stackPtr,x,--y);
         printf("%d\t%d\n",x,y);
      }
      else if(map[x-1][y]==0&&checkStack(stackPtr,x-1,y)!=0&&x>0)
      {
         push(&stackPtr,--x,y);
         printf("%d\t%d\n",x,y);
      }
      else
      {
         if(isEmpty()!=0)
            pop(&stackPtr);
      }
      if(x==6&&y==6)
         break;
   }
   push(&stackPtr,x,y);
   printmap(stackPtr);
   return 0;
}

void load(void)
{
   FILE *cfPtr;
   if((cfPtr=fopen("maze.in.txt","r"))==NULL)
   {
      printf("load file ERROR");
   }
   else
   {
      while(!feof(cfPtr))
      {
         fscanf(cfPtr,"%d",&point);
         map[column][row]=point;
         row++;
         if(row==7)
         {
            row=0;
            column++;
         }
      }
      fclose(cfPtr);
   }  
}

void push(StackNodePtr *topPtr,int x,int y)
{
   StackNodePtr newPtr;
   newPtr=malloc(sizeof(StackNode));

   newPtr->mapx=x;
   newPtr->mapy=y;
   map[newPtr->mapx][newPtr->mapy]=3;
   newPtr->nextPtr=*topPtr;
   *topPtr=newPtr;
}

void pop(StackNodePtr *topPtr)
{
   StackNodePtr tempPtr=*topPtr;
   
   map[tempPtr->mapx][tempPtr->mapy]=2;
   
   if(tempPtr!=NULL&&tempPtr->nextPtr!=NULL)
   {
      x+=((tempPtr->nextPtr->mapx)-(tempPtr->mapx));
      y+=((tempPtr->nextPtr->mapy)-(tempPtr->mapy));
   }
   
   *topPtr=(*topPtr)->nextPtr;
   free(tempPtr);
}

void printmap(StackNodePtr currentPtr)
{
   FILE *cfPtr1;
   int i=0,j=0;
   if((cfPtr1=fopen("maze.out.txt","w"))==NULL)
   {
      printf("File ERROR");
   }
   else
   {
      for(i=0;i<7;i++)
      {
         for(j=0;j<7;j++)
         {
            if(map[i][j]==2)
            {
               map[i][j]=0;
            }
            
            if(map[i][j]==3)
            {
               fprintf(cfPtr1,"* ");
               printf("* ");
            }
            else
            {
               fprintf(cfPtr1,"%d ",map[i][j]);
               printf("%d ",map[i][j]);
            }
         }
         fprintf(cfPtr1,"\n");
         printf("\n");
      }
      fclose(cfPtr1);
   }
}

int checkStack(StackNodePtr currentPtr,int checkx,int checky)
{
   if(currentPtr==NULL)
   {
      printf("Stack:Empty\n");
      return 0;
   }
   else
   {
      if(currentPtr->nextPtr!=NULL)
         currentPtr=currentPtr->nextPtr;
      if(currentPtr->mapx==checkx&&currentPtr->mapy==checky)
      {
         return 0;
      }
      else
      {
         return 1;
      }
   }
}

int isEmpty(void)
{
   return stackPtr==NULL?0:1;
}
