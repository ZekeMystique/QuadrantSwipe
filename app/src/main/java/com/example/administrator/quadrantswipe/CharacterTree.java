package com.example.administrator.quadrantswipe;

/**
 * Created by Rohan Dawson on 13/11/2015.
 */
public class CharacterTree {

    TreeNode root;
    TreeNode pointer;
    StringBuffer outputBuffer;

    CharacterTree()
    {
        this(new String[]{"z", "v", "x", "k", "c", "p", "m", "w", "q", ".", "j", "," , "f", "g", "y", "b",
                "d", "u", "h", "l", "a", "e", "i", "o", "t", "n", "s", "r"});
    }

    CharacterTree(String charArray[])
    {
        //Create array of TreeNode leaves
        TreeNode[] nodeArray = new TreeNode[28];

        //Add an assert statement to say charArray.length() = 28

        //Initialise all leaves
        for (int i=0; i<28; i++)
            nodeArray[i] = new TreeNode(charArray[i], true);

        //Create and initialise branches
        TreeNode br = new TreeNode(null, false, nodeArray[24], nodeArray[25], nodeArray[26], nodeArray[27]);
        TreeNode bl = new TreeNode(null, false, nodeArray[20], nodeArray[21], nodeArray[22], nodeArray[23]);
        TreeNode tr = new TreeNode(null, false, nodeArray[16], nodeArray[17], nodeArray[18], nodeArray[19]);
        TreeNode tl = new TreeNode(null, false, null, null, null, null);
        tl.topLeft = new TreeNode(null, false, nodeArray[0], nodeArray[1], nodeArray[2], nodeArray[3]);
        tl.topRight = new TreeNode(null, false, nodeArray[4], nodeArray[5], nodeArray[6], nodeArray[7]);
        tl.bottomLeft = new TreeNode(null, false, nodeArray[8], nodeArray[9], nodeArray[10], nodeArray[11]);
        tl.bottomRight = new TreeNode(null, false, nodeArray[12], nodeArray[13], nodeArray[14], nodeArray[15]);
        root = new TreeNode(null, false, tl, tr, bl, br);

        //Initialise pointer
        pointer = root;

        //Initialise output buffer
        outputBuffer = new StringBuffer();
    }

    private class TreeNode
    {
        String data;
        boolean leaf;
        TreeNode topLeft;
        TreeNode topRight;
        TreeNode bottomLeft;
        TreeNode bottomRight;

        TreeNode(String data, boolean leaf)
        {
            this.data = data;
            this.leaf = leaf;
        }

        TreeNode(String data, boolean leaf, TreeNode topLeft, TreeNode topRight, TreeNode bottomLeft, TreeNode bottomRight)
        {
            this.data = data;
            this.leaf = leaf;
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
        }
    }

    public String onTopLeftSwipe()
    {
        pointer = pointer.topLeft;

        if (pointer.leaf)
        {
            String result = pointer.data;
            outputBuffer.append(result);
            pointer = root;
            return result;
        }
        else
            return null;
    }

    public String onTopRightSwipe()
    {
        pointer = pointer.topRight;

        if (pointer.leaf)
        {
            String result = pointer.data;
            pointer = root;


            return result;
        }
        else
            return null;
    }

    public String onBottomLeftSwipe()
    {
        pointer = pointer.bottomLeft;

        if (pointer.leaf)
        {
            String result = pointer.data;
            pointer = root;
            return result;
        }
        else
            return null;
    }

    public String onBottomRightSwipe()
    {
        pointer = pointer.bottomRight;

        if (pointer.leaf)
        {
            String result = pointer.data;
            pointer = root;
            return result;
        }
        else
            return null;
    }

    public String print()
    {
        StringBuffer outputBuffer = new StringBuffer();
        print(root, outputBuffer);
        return outputBuffer.toString();
    }

    public StringBuffer print(TreeNode subtree, StringBuffer outputBuffer)
    {

        if (subtree != null)
        {
            if (subtree.data != null)
            {
                outputBuffer.append(subtree.data);
                outputBuffer.append(" ");
            }

            print(subtree.topLeft, outputBuffer);
            print(subtree.topRight, outputBuffer);
            print(subtree.bottomLeft, outputBuffer);
            print(subtree.bottomRight, outputBuffer);
        }
        return outputBuffer;
    }



}

