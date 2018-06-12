package com.brandon3055.brandonscore.client.gui.modulargui.markdown.reader.visitor.property;

import java.util.List;

/**
 * Created by brandon3055 on 5/30/2018.
 */
public abstract class ImageVisitor extends PropertyVisitor {

    @Override
    public abstract void visitBorderColour(int argb);

    @Override
    public abstract void visitBorderColourHover(int argb);

    @Override
    public abstract void visitLeftPad(int leftPadding);

    @Override
    public abstract void visitRightPad(int rightPadding);

    @Override
    public abstract void visitTopPad(int topPadding);

    @Override
    public abstract void visitBottomPad(int bottomPadding);

    @Override
    public abstract void visitWidth(int width, boolean screenRelative);

    @Override
    public abstract void visitHeight(int height);

    @Override
    public abstract void visitTooltip(List<String> tooltip);

    public abstract void visitLinkTo(String linkTo);
}
