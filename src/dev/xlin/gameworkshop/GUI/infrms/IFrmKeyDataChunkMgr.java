package dev.xlin.gameworkshop.GUI.infrms;

import dev.xlin.gameworkshop.GUI.dialog.DlgKeyDataChunkDef;
import dev.xlin.gameworkshop.GUI.fast;
import dev.xlin.gameworkshop.GUI.frmMain;
import dev.xlin.gameworkshop.GUI.guiIFrameControl;
import dev.xlin.gameworkshop.progs.foundation.beans.BeanKeyDataChunk;
import dev.xlin.gameworkshop.progs.foundation.keyDataChunk;
import dev.xlin.gameworkshop.progs.iConst;
import dev.xlin.swingTools2.guiCommon;
import dev.xlin.swingTools2.myTreeNode;
import dev.xlin.tols.data.wakeup;
import dev.xlin.tols.interfaces.iDAO;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class IFrmKeyDataChunkMgr extends javax.swing.JInternalFrame
{

    private wakeup up = null;
    private JDesktopPane desk = null;
    private List lcs = null;

    public IFrmKeyDataChunkMgr(wakeup _up, JDesktopPane _desk)
    {
        initComponents();
        up = _up;
        desk = _desk;
        initGUI();
    }

    private void initGUI()
    {
        this.setLocation(desk.getWidth() / 2 - this.getWidth() / 2,
                desk.getHeight() / 2 - this.getHeight() / 2);
        setFrameIcon(frmMain.getIcon());
        makeTree();
    }

    private void makeTree()
    {
        myTreeNode mtn = new myTreeNode("[键值数据区块目录]", 0, 0);
        keyDataChunk kdc = new keyDataChunk(up);
        lcs = kdc.getAllRecord();
        if (lcs != null)
        {
            for (int i = 0; i < lcs.size(); i++)
            {
                BeanKeyDataChunk bean = (BeanKeyDataChunk) lcs.get(i);
                String s = bean.getChunkName();
                if (bean.getStatus() != iDAO.OBJECT_STATE_ACTIVE)
                {
                    s = s + "  " + iConst.transDAOState(bean.getStatus());
                }
                myTreeNode mck = new myTreeNode(s, bean.getOID(), 1);
                mtn.add(mck);
            }
        }
        DefaultTreeModel dtm = new DefaultTreeModel(mtn);
        treeChunks.setModel(dtm);
        guiCommon.expandTree(treeChunks);
    }

    private void doNewChunk()
    {
        DlgKeyDataChunkDef dlg = new DlgKeyDataChunkDef(null, true, up, null);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private BeanKeyDataChunk getSelectedChunk()
    {
        TreePath tph = treeChunks.getSelectionPath();
        if (tph == null)
        {
            return null;
        }
        myTreeNode mtn = (myTreeNode) tph.getLastPathComponent();
        if (mtn.getNodeType() == 0)
        {
            return null;
        }
        int oid = mtn.getNodeOID();
        keyDataChunk kdc = new keyDataChunk(up);
        return (BeanKeyDataChunk) kdc.getRecordByID(oid);
    }

    private void doEditChunk()
    {
        BeanKeyDataChunk bean = getSelectedChunk();
        if (bean == null)
        {
            return;
        }
        DlgKeyDataChunkDef dlg = new DlgKeyDataChunkDef(null, true, up, bean);
        dlg.setVisible(true);
        if (dlg.getOK())
        {
            makeTree();
        }
        dlg.dispose();
        dlg = null;
    }

    private void doDeleteChunk()
    {
        BeanKeyDataChunk bean = getSelectedChunk();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否销毁这个区块数据？\n销毁以后的区块数据不可被恢复，原区块数据会被指向未知或丢失的区块，同时失去区块设置的功能。\n\n"
                + "是否继续执行销毁操作？");
        if (sel != fast.YES)
        {
            return;
        }
        keyDataChunk kdc = new keyDataChunk(up);
        int r = kdc.destroyBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
            fast.msg("销毁操作完成。");
        }
        else
        {
            fast.err("销毁操作失败", r);
        }
    }

    private void doDisChunk()
    {
        BeanKeyDataChunk bean = getSelectedChunk();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要将这个区块设置为失效？");
        if (sel != fast.YES)
        {
            return;
        }
        keyDataChunk kdc = new keyDataChunk(up);
        int r = kdc.deleteRecord(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
            fast.msg("失效操作完成。");
        }
        else
        {
            fast.err("失效操作失败", r);
        }
    }

    private void doRevChunk()
    {
        BeanKeyDataChunk bean = getSelectedChunk();
        if (bean == null)
        {
            return;
        }
        int sel = fast.ask("是否要将这个区块恢复为有效？");
        if (sel != fast.YES)
        {
            return;
        }
        keyDataChunk kdc = new keyDataChunk(up);
        int r = kdc.revertBean(bean.getOID());
        if (r == iDAO.OPERATE_SUCCESS)
        {
            makeTree();
            fast.msg("恢复操作完成。");
        }
        else
        {
            fast.err("恢复操作失败", r);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        popKey = new javax.swing.JPopupMenu();
        miNewChunk = new javax.swing.JMenuItem();
        miEditChunk = new javax.swing.JMenuItem();
        miDisChk = new javax.swing.JMenuItem();
        miRevChk = new javax.swing.JMenuItem();
        miRemoveChunk = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeChunks = new javax.swing.JTree();

        miNewChunk.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miNewChunk.setText("新建区块");
        miNewChunk.setToolTipText("");
        miNewChunk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miNewChunkActionPerformed(evt);
            }
        });
        popKey.add(miNewChunk);

        miEditChunk.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miEditChunk.setText("修改区块");
        miEditChunk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miEditChunkActionPerformed(evt);
            }
        });
        popKey.add(miEditChunk);

        miDisChk.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miDisChk.setText("失效区块");
        miDisChk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miDisChkActionPerformed(evt);
            }
        });
        popKey.add(miDisChk);

        miRevChk.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRevChk.setText("恢复区块");
        miRevChk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRevChkActionPerformed(evt);
            }
        });
        popKey.add(miRevChk);

        miRemoveChunk.setFont(new java.awt.Font("微软雅黑", 0, 12)); // NOI18N
        miRemoveChunk.setText("删除区块");
        miRemoveChunk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                miRemoveChunkActionPerformed(evt);
            }
        });
        popKey.add(miRemoveChunk);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("键值存储区块定义管理");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
            {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });

        treeChunks.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                treeChunksMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(treeChunks);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void treeChunksMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_treeChunksMouseClicked
    {//GEN-HEADEREND:event_treeChunksMouseClicked
        if (evt.getButton() != evt.BUTTON1)
        {
            popKey.show(treeChunks, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_treeChunksMouseClicked

    private void miNewChunkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miNewChunkActionPerformed
    {//GEN-HEADEREND:event_miNewChunkActionPerformed
        doNewChunk();
    }//GEN-LAST:event_miNewChunkActionPerformed

    private void miEditChunkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miEditChunkActionPerformed
    {//GEN-HEADEREND:event_miEditChunkActionPerformed
        doEditChunk();
    }//GEN-LAST:event_miEditChunkActionPerformed

    private void miRemoveChunkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRemoveChunkActionPerformed
    {//GEN-HEADEREND:event_miRemoveChunkActionPerformed
        doDeleteChunk();
    }//GEN-LAST:event_miRemoveChunkActionPerformed

    private void miDisChkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDisChkActionPerformed
    {//GEN-HEADEREND:event_miDisChkActionPerformed
        doDisChunk();
    }//GEN-LAST:event_miDisChkActionPerformed

    private void miRevChkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRevChkActionPerformed
    {//GEN-HEADEREND:event_miRevChkActionPerformed
        doRevChunk();
    }//GEN-LAST:event_miRevChkActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        guiIFrameControl.setIsKeyChunkMgr(false);
    }//GEN-LAST:event_formInternalFrameClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem miDisChk;
    private javax.swing.JMenuItem miEditChunk;
    private javax.swing.JMenuItem miNewChunk;
    private javax.swing.JMenuItem miRemoveChunk;
    private javax.swing.JMenuItem miRevChk;
    private javax.swing.JPopupMenu popKey;
    private javax.swing.JTree treeChunks;
    // End of variables declaration//GEN-END:variables
}
