#include<stdio.h>
#include<stdlib.h>
#include<string.h>

void MaxHeap(int tree[],int lastFnode,int size);
void HeapSort(int tree[],int size);

int main(void)
{
	char input[100];
	int tree[100];//�𪺴`�Ǫ�ܪk 
	int size=1;
	int index;
	char *tokenPtr;
	int temp;
	
	printf("Please input a number of sequence, split by space:");
	gets(input);//���o��J�s��input
	
	tokenPtr=strtok(input," ");//�Ninput�H�Ů���� 
	while(tokenPtr!=NULL)
	{
		tree[size++]=atoi(tokenPtr);
		tokenPtr=strtok(NULL," ");//�~����� 
	}

	for(index=size/2;index>0;index--)
	{
		MaxHeap(tree,index,size-1);
	}
	
	printf("Level Order:");
	for(index=1;index<size;index++)
	{
		printf("%d ",tree[index]);
	}
	printf("\n");
	
	HeapSort(tree,size);
	printf("Heap Sort:");
	for(index=1;index<size;index++)
	{
		printf("%d ",tree[index]);
	}
	printf("\n");
	return 0;
} 


void HeapSort(int tree[],int size)
{
	int index=0;
	int temp=0;
	for(index=size-2;index>0;index--)
	{
		temp=tree[index+1];
		tree[index+1]=tree[1];
		tree[1]=temp;
		MaxHeap(tree,1,index);
		
	}
}

void MaxHeap(int tree[],int lastFnode,int size)
{
	int des=0;
	int temp=0;
	des=2*lastFnode;
	temp=tree[lastFnode];
	
	while(des<=size)
	{
		if(des<size)
		{
			if(tree[des]<tree[des+1])
				des++;
		}
		if(temp>=tree[des])
		{
			break;
		}
		else
		{
			tree[des/2]=tree[des];
			des*=2;
		}
		tree[des/2]=temp;
	}
}
