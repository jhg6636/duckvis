package com.catshi.bob.ui.command;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\bg\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003J\u0017\u0010\u0004\u001a\u0004\u0018\u00018\u00002\u0006\u0010\u0005\u001a\u00020\u0006H&\u00a2\u0006\u0002\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcom/catshi/bob/ui/command/CommandParser;", "T", "Lcom/catshi/bob/ui/command/BobCommand;", "", "fromText", "text", "", "(Ljava/lang/String;)Lcom/catshi/bob/ui/command/BobCommand;", "bob"})
@org.springframework.stereotype.Component
public abstract interface CommandParser<T extends com.catshi.bob.ui.command.BobCommand> {
    
    @org.jetbrains.annotations.Nullable
    public abstract T fromText(@org.jetbrains.annotations.NotNull
    java.lang.String text);
}