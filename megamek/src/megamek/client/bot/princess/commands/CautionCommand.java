/*
 * MegaMek - Copyright (c) 2025 - The MegaMek Team. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 */
package megamek.client.bot.princess.commands;

import megamek.client.bot.Messages;
import megamek.client.bot.princess.Princess;
import megamek.server.commands.arguments.Argument;
import megamek.server.commands.arguments.Arguments;
import megamek.server.commands.arguments.IncDecSetIntegerArgument;

import java.util.List;

/**
 * Command to set the caution level of the bot.
 * @author Luana Coppio
 */
public class CautionCommand implements ChatCommand {

    private static final String CAUTION = "caution";

    @Override
    public List<Argument<?>> defineArguments() {
        return List.of(
            new IncDecSetIntegerArgument(
                CAUTION,
                Messages.getString("Princess.command.caution.caution"),
                0,
                10)
        );
    }

    @Override
    public void execute(Princess princess, Arguments arguments) {

        IncDecSetIntegerArgument value = arguments.get(CAUTION, IncDecSetIntegerArgument.class);
        int currentIndex = princess.getBehaviorSettings().getFallShameIndex();

        if (value.getOperation().equals(IncDecSetIntegerArgument.Operation.SET)) {
            princess.getBehaviorSettings().setFallShameIndex(value.getValue());
        } else {
            princess.getBehaviorSettings().setFallShameIndex(
                princess.getBehaviorSettings().getFallShameIndex() + value.getValue());
        }

        princess.sendChat(Messages.getString("Princess.command.caution.cautionChanged",
            currentIndex, princess.getBehaviorSettings().getFallShameIndex()));
    }
}
