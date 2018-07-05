# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [1.4.3] - 2018-07-05
### Fixed
- NPE in OperationQueryBuilder.

## [1.4.2] - 2018-06-07
### Added
- URL field to operation and template entities.
- Multidex support.

### Changed
- InputType of dateGroup and valueGroup fields to numberSigned.
- Updated iconics lib and fontawesome typeface versions.
- Updated icon names.
- Updated versions of other libs (dependencies).

## [1.4.1] - 2018-06-06
### Changed
- Database version for auto creating t_sms_pattern table.

## [1.4.0] - 2018-06-06
### Added
- Type, number, payment_system and card_number fields to account entity.
- Ability to select template if it is regularSelectable.
- Sms pattern entity and view.
- Sms receiver, parser, notification builder and sender.

### Changed
- Increased gradle and gradle-wrapper versions.
- Increased support lib version.
- Increased preference compat lib version.
- Optimized layouts initializing.

### Removed
- Unnecessary code.
- Unnecessary initial layouts.

### Fixed
- Long to Integer cast exception.
- Showed class name of OperationViewDestroyer in console.
- Template item icon.

## [1.3.4] - 2018-02-11
### Fixed
- Fatal signal 11 (SIGSEGV) at 0xdeadd00d (code=1), thread 15725 (Compiler) described at [StackOverflow.com](https://stackoverflow.com/q/47904589/8035065).

## [1.3.3] - 2017-12-20
### Changed
- View account's icon if operation's icon is empty.

## [1.3.2] - 2017-12-18
### Changed
- Clarified titles of charts, filters and displays.

### Fixed
- NPE when searching for fragments in the chart activity.

## [1.3.1] - 2017-12-15
### Changed
- Enabled ProGuard (without obfuscation) to reduce the number of methods.

## [1.3.0] - 2017-12-15
### Added
- Field *exchange_rate_value* to the view *v_operation*.
- Destroyer of views.
- Charts.

### Changed
- Renamed OperationCalculator to BalanceCalculator for clarification.
- Creation and destruction of views: it moved to the trampoline scheduler.

### Fixed
- Scrolling in the filter dialogs.

## [1.2.2] - 2017-11-30
### Added
- Calculator to fields that accept decimal numbers.

### Removed
- DroidParts dependency.

## [1.2.1] - 2017-11-29
### Added
- Credits.

## [1.2.0] - 2017-11-29
### Added
- Templates.

### Changed
- Optimized onActivityResult() methods.

## [1.1.2] - 2017-11-28
### Added
- Bindable annotation to field 'parent' of account, article and person entities.

### Removed
- Unnecessary code.

## [1.1.1] - 2017-11-28
### Removed
- Unnecessary code.

### Fixed
- Adding operations from dashboard without filter.
- Popup menu for folder entities.

## [1.1.0] - 2017-11-27
### Added
- Possibility to duplicate any operation.
- Get default field values for new operations from appropriate filter if defined.
- Changelog.

### Changed
- Developer email.

### Removed
- Unused FlowOfFundsOperationProvider.java.
- Default values (except for article) from all operations filters.

### Fixed
- Link to internal entity in javadoc.
- Adding operations from dashboard activity.

## [1.0.2] - 2017-11-23
### Added
- Add quick links and issue checklist to readme.

## [1.0.1] - 2017-11-22
### Added
- Licence.
- Readme.

### Changed
- Allow to edit active field of the account.
- Load owner and currency only for regular accounts when creating a new one.

## [1.0.0] - 2017-11-22
### Added
- Initial version.
