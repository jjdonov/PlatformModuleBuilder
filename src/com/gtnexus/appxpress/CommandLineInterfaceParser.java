package com.gtnexus.appxpress;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

/**
 * Wrapper to tie together CLIOptions and the Apache Commons CLI library.
 * 
 * @author jdonovan
 *
 */
public class CommandLineInterfaceParser <T extends CLIOption> {

	private final String[] userArgs;
	private final Set<T> cliOptionSet;
	private final Options options;
	private CommandLine cmd;

	/**
	 * 
	 * @param userArgs
	 *            the args provided to main() by the user when invoking this
	 *            tool
	 * @param cliOptionSet
	 *            the option set defining what can be passed to this tool
	 */
	public CommandLineInterfaceParser(String[] userArgs,
			Set<T> cliOptionSet) {
		this.userArgs = userArgs;
		this.cliOptionSet = cliOptionSet;
		this.options = new Options();
		for (CLIOption opt : cliOptionSet) {
			options.addOption(Option.builder(opt.getName()).type(opt.getType())
					.desc(opt.getDescription()).hasArg(opt.hasArg())
					.required(false).build());
		}
	}

	/**
	 * 
	 * @return CommandLine created from user input and the option set.
	 * @throws PMExtractorException
	 *             if the CommandLine is not parsable.
	 */
	public CommandLine getCommandLine() throws PMExtractorException {
		if (cmd == null) {
			parseCommandLine();
		}
		return cmd;
	}

	/**
	 * Parses the args provided by the user according to the provided option
	 * set.
	 * 
	 * @throws PMExtractorException
	 *             if input is not parasable.
	 */
	public void parseCommandLine() throws PMExtractorException {
		CommandLineParser parser = new DefaultParser();
		try {
			cmd = parser.parse(options, userArgs);
		} catch (ParseException e) {
			throw new PMExtractorException(
					"Exception when parsing args from command line!");
		}
	}

	/**
	 * Checks to see if the user provided this CLIOption.
	 * 
	 * @param opt
	 *            the CLIOption being checked for.
	 * @return false if parsing has not yet been performed.
	 */
	public boolean hasOption(CLIOption opt) {
		if (cmd == null) {
			return false;
		}
		return cmd.hasOption(opt.getName());
	}


	public Map<T, String> getOptionsMap() {
		if (cmd == null) {
			return Collections.emptyMap();
		}
		Map<T, String> optMap = new HashMap<>();
		for(T opt : cliOptionSet) {
			if(cmd.hasOption(opt.getName())) {
				optMap.put(opt, cmd.getOptionValue(opt.getName()));
			}
		}
		return optMap;
	}
	
	public Set<T> getCliOptionSet() {
		return cliOptionSet;
	}


	/**
	 * Displays the usage information and exits.
	 */
	public void displayHelpAndExit() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("pmextractor", options);
		System.exit(0);
	}

}
